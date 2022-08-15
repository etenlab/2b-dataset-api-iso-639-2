package org.eten.datasetapiiso6392.component

import org.eten.datasetapiiso6392.common.ErrorType
import org.eten.datasetapiiso6392.common.Utility
import org.eten.datasetapiiso6392.core.AppConfig
import org.eten.datasetapiiso6392.core.KafkaService
import org.eten.datasetapiiso6392.core.KafkaTopics
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.DependsOn
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource

@RestController
@DependsOn("DatabaseSync")
class Login(
    @Autowired
    val app_config: AppConfig,

    @Autowired
    @Qualifier("readerDataSource")
    val writer_ds: DataSource,

    @Autowired
    @Qualifier("readerDataSource")
    val reader_ds: DataSource,

    @Autowired val util: Utility,

    @Autowired val kafka: KafkaService,
) {
  val writer_jdbc = NamedParameterJdbcTemplate(writer_ds)
  val reader_jdbc = NamedParameterJdbcTemplate(reader_ds)

  @GetMapping("/api/iso-639-2/find-by-iso-639-2/{term}")
  @ResponseBody
  fun find_by_iso_639_2(@Param("term") term: String): ListResponse {
    try {
      if (term.isEmpty()) return ListResponse(ErrorType.TermInvalid)

      //language=SQL
      val result = writer_jdbc.queryForRowSet("""
          select * 
          from iso_639_2
          where iso_639_2 = :term;
        """.trimIndent(), MapSqlParameterSource()
          .addValue("term", term)
      )

      val items = mutableListOf<Iso6392Entry>()

      while (result.next()) {
        items.add(
            Iso6392Entry(
                id = result.getLong("id"),
                iso_639_2 = result.getString("iso_639_2")!!,
                entry_type = if (result.getString("entry_type") != null) {
                  Iso_639_2_entry_type.valueOf(result.getString("entry_type")!!)
                } else {
                  null
                },
                iso_639_1 = result.getString("iso_639_1"),
                english_name = result.getString("english_name"),
                french_name = result.getString("french_name"),
                german_name = result.getString("german_name"),
            )
        )
      }

      return ListResponse(
          error = ErrorType.NoError,
          items = items,
      )

    } catch (e: Exception) {
      kafka.send(KafkaTopics.Error, e.localizedMessage + '\n' + e.stackTrace
          .map { it.toString() }
          .reduce { acc, s -> acc + '\n' + s })
    }

    return ListResponse(ErrorType.UnknownError)
  }

  @GetMapping("/api/iso-639-2/find-by-english-name/{term}")
  @ResponseBody
  fun find_by_english_name(@Param("term") term: String): ListResponse {
    try {
      if (term.isEmpty()) return ListResponse(ErrorType.TermInvalid)

      //language=SQL
      val result = writer_jdbc.queryForRowSet("""
          select * 
          from iso_639_2
          where english_name like :term;
        """.trimIndent(), MapSqlParameterSource()
          .addValue("term", "%$term%")
      )

      val items = mutableListOf<Iso6392Entry>()

      while (result.next()) {
        items.add(
            Iso6392Entry(
                id = result.getLong("id"),
                iso_639_2 = result.getString("iso_639_2")!!,
                entry_type = if (result.getString("entry_type") != null) {
                  Iso_639_2_entry_type.valueOf(result.getString("entry_type")!!)
                } else {
                  null
                },
                iso_639_1 = result.getString("iso_639_1"),
                english_name = result.getString("english_name"),
                french_name = result.getString("french_name"),
                german_name = result.getString("german_name"),
            )
        )
      }

      return ListResponse(
          error = ErrorType.NoError,
          items = items,
      )

    } catch (e: Exception) {
      kafka.send(KafkaTopics.Error, e.localizedMessage + '\n' + e.stackTrace
          .map { it.toString() }
          .reduce { acc, s -> acc + '\n' + s })
    }


    return ListResponse(ErrorType.UnknownError)
  }
}
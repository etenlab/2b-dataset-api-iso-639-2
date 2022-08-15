package org.eten.datasetapiiso6392.common

import org.eten.datasetapiiso6392.core.AppConfig
import org.eten.datasetapiiso6392.core.KafkaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class Utility(
    @Autowired
    val app_config: AppConfig,

    @Autowired
    @Qualifier("readerDataSource")
    val writer_ds: DataSource,

    @Autowired
    @Qualifier("readerDataSource")
    val reader_ds: DataSource,

    @Autowired
    val kafka: KafkaService,
) {
  val writer_jdbc = NamedParameterJdbcTemplate(writer_ds)
  val reader_jdbc = NamedParameterJdbcTemplate(reader_ds)

  fun random_string(length: Int): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    val token = (1..length)
        .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
    return token
  }
}
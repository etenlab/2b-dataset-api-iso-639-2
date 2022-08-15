package org.eten.datasetapiiso6392

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@Configuration
@ConfigurationPropertiesScan
@EnableScheduling
class DatasetAPIISO6392Application

fun main(args: Array<String>) {
	runApplication<DatasetAPIISO6392Application>(*args)
}

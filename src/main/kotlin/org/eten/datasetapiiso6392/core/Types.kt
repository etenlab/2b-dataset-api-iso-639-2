package org.eten.datasetapiiso6392.core

enum class ConfigEnv {
  local,
  test,
  prod,
}

enum class KafkaTopics {
  InstanceInfo,
  Error,
}
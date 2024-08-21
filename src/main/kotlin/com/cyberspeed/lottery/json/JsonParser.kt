package com.cyberspeed.lottery.json

import com.cyberspeed.lottery.data.Lottery
import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

val mapper: ObjectMapper = ObjectMapper().registerKotlinModule()

fun parseLotteryConfig(string: String?): Lottery {
    return mapper.readValue(string, Lottery::class.java)
} 
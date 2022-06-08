package org.karamelsoft.research.axon.libraries.service.api

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.databind.ser.std.StdSerializer

private const val _type: String = "_type"
private const val value: String = "value"

class OkSerializer(): StdSerializer<Ok<*>>(Ok::class.java) {

    override fun serialize(ok: Ok<*>, jgen: JsonGenerator, serializerProvider: SerializerProvider) {
        jgen.writeStartObject()
        jgen.writeStringField(_type, ok.value!!::class.java.name)
        jgen.writeObjectField(value, ok.value)
        jgen.writeEndObject()
    }
}

class OkDeserializer(): StdDeserializer<Ok<*>>(Ok::class.java) {

    override fun deserialize(parser: JsonParser, context: DeserializationContext): Ok<*> {
        val node: TreeNode = parser.codec.readTree(parser)
        val type = (node.get(_type) as TextNode).asText()
        val typeClass = Thread.currentThread().contextClassLoader.loadClass(type)
        val valueAsText = node.get(value).toString()
        val value = (parser.codec as ObjectMapper).readValue(valueAsText, typeClass)

        return Ok(value)
    }
}

package za.co.k0ma.benfordchecker.config

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.core.util.Separators
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

val OBJECT_MAPPER = buildJacksonMapper()

fun buildJacksonMapper(): JsonMapper {
    val printer =
        DefaultPrettyPrinter()
            .withSeparators(
                Separators
                    .createDefaultInstance()
                    .withObjectFieldValueSpacing(Separators.Spacing.AFTER),
            )
    printer.indentObjectsWith(DefaultIndenter("  ", DefaultIndenter.SYS_LF))

    val builder = JsonMapper.builder()
    builder.configure(SerializationFeature.INDENT_OUTPUT, true)
    builder.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    builder.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
    builder.addModule(KotlinModule.Builder().build())
    builder.defaultPrettyPrinter(printer)

    return builder.build()
}

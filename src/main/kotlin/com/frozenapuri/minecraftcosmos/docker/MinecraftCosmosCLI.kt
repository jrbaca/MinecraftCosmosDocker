package com.frozenapuri.minecraftcosmos.docker


import mu.KotlinLogging
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import java.util.*

/**
 * The command line parser used to extract the args and construct the appropriate execution path.
 */
object MinecraftCosmosCLI {

    // TODO investigate main level command instead of flag
    private val log = KotlinLogging.logger {}

    private val MODE_SHORT_OPT = "m"
    private val MODE_LONG_OPT = "mode"

    private val options = object : Options() {
        init {
            addRequiredOption(MODE_SHORT_OPT, MODE_LONG_OPT, true,
                    "Main execution branch to take: <setup/shutdown>")
        }
    }

    /**
     * Parses the provided command line args.
     *
     * @param args to parse
     * @return [Executable] to be run
     */
    fun parseCommandLine(args: Array<String>): Executable {
        log.info(String.format("Will now parse args: ${args.contentToString()}"))
        return createExecutableFromCommandLine(parseArgs(args))
    }

    private fun parseArgs(args: Array<String>): CommandLine {
        try {
            val commandLineParser = DefaultParser()
            return commandLineParser.parse(options, args)
        } catch (e: ParseException) {
            throw AutomaticMinecraftParseException(
                    "Was unable to parse the provided arguments", e)
        }

    }

    private fun createExecutableFromCommandLine(commandLine: CommandLine): Executable {
        when (commandLine.getOptionValue(MODE_SHORT_OPT)) {
            "setup" -> return Setup(UUID.nameUUIDFromBytes("first-server2".toByteArray()))
            "shutdown" -> return Shutdown(UUID.nameUUIDFromBytes("first-server2".toByteArray()))
            else -> throw AutomaticMinecraftParseException("Invalid mode provided")
        }
    }
}

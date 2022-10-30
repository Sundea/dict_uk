package org.dict_uk.expand

import org.apache.commons.cli.Option

import groovy.transform.TypeChecked
import java.util.regex.Pattern
import groovy.cli.picocli.CliBuilder

//@TypeChecked
class Args {
	boolean corp
	boolean mfl
	boolean indent
	boolean time
	boolean stats
	boolean log_usage
	boolean wordlist
	boolean flush
	String affixDir
	String dictDir
	List<String> removeWithTags = []
	Pattern removeWithRegex
	List<String> removeTags = ["v-u", "np", "coll"]
	List<String> removeTagsWithColons = []
	List<String> lemmaForTags = ["advp"]


	public static Args args = new Args();
	
	static {
		args.removeWithTags = []
		args.lemmaForTags = ["advp"]
		args.removeTags = ["v-u", "np", "coll"]
	}

	
	public static parse(String[] argv) {
		def cli = new CliBuilder(usage: 'expandAll <flags> or expandAll -h')

		cli._(longOpt:'aff', args:1, required: true, 'Affix dir')
		cli._(longOpt:'dict', args:1, required: true, 'Dictionary dir')

		cli._(longOpt:'mfl', 'Generate morfologik format of the dictionary')
		cli._(longOpt:'indent', 'Generate indented format of the dictionary')

		cli._(longOpt:'stats', 'Generate dictionary statistics')
		cli._(longOpt:'wordlist', 'Generate word, lemma, and tag list files')

		cli._(longOpt:'time', 'Print timing info')
		cli._(longOpt:'log-usage', 'Generate affix usage statistics file')

		cli._(longOpt:'removeWithTags', type: String, args: 1, argName:'tags', 'Remove forms with listed tags (comma separated)')
		cli._(longOpt:'removeWithRegex', type: String, args: 1, argName:'regex', 'Remove forms that match regular expression')
		cli._(longOpt:'removeTags', type: String, args: 1, argName:'tags', 'Remove listed tags (comma separated) from generated forms')
		cli._(longOpt:'lemmaForTags', type: String, args: 1, argName:'tags', 'Promote generated forms with listed tags (comma separated) to separate lemmas (supported tags: advp, compr)')

		cli.f(longOpt:'flush', 'Flush output after each input line (interactive mode)')
		
		cli.h(longOpt: 'help', 'Help - Usage Information')


		def options = cli.parse(argv)

		if (!options || options.h) {
			cli.usage()
			System.exit(0)
		}

		args = new Args()
		args.with {
			mfl = options.mfl
			indent = options.indent
			time = options.time
			stats = options.stats
			log_usage = options['log-usage']
			wordlist = options.wordlist
			flush = options.flush

			affixDir = options.aff
			dictDir = options.dict
			
			if( options.removeWithTags ) {
				removeWithTags = options.removeWithTags.split(",")
			}

			if( options.removeWithRegex ) {
				removeWithRegex = ~options.removeWithRegex
				System.err.println("got removeWithRegex " + removeWithRegex + " - " + options.removeWithRegex)
			}
			
			if( options.removeTags )
				removeTags = options.removeTags.split(",")
				
			if( options.lemmaForTags ) 
				lemmaForTags = options.lemmaForTags.split(",")

            removeTagsWithColons = removeTags.collect { ':' + it }
		}
	}
}

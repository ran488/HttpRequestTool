import static groovyx.net.http.ContentType.URLENC
import groovy.io.FileType
import groovyx.net.http.HTTPBuilder


class ServletPoster {

	private static String ENDPOINT_URL = "default";
	private static String INITIAL_SCAN_DIR = "src/dist/requests";
	private static String HTTP_PATH = "/default";
	private static final String APPLICATION_PROPERTIES = "../application.properties";
	def configSlurper;

	/** Request file closure - look for files ending in .request extension */
	def withEachRequest(Closure closure) {
		new File(INITIAL_SCAN_DIR).eachFileRecurse(FileType.FILES) {
			if (it.name =~ /\.request$/) {
				closure.call(it)
			}
		}
	}

	public ServletPoster() {
		configSlurper = new ConfigSlurper()
		def appProps
		try {
			appProps = configSlurper.parse(new File(APPLICATION_PROPERTIES).toURL())
			println "Overriding properties with: ${appProps}"
			ENDPOINT_URL = appProps.endpoint.url
			HTTP_PATH = appProps.endpoint.path
			INITIAL_SCAN_DIR = appProps.requests_dir
		} catch (FileNotFoundException fnf) {
			println 'Could not find application.properties, using the defaults (DEV).'
		}
	}

	public void run() {
		withEachRequest() { req ->
			println "**** BEGIN request: ${req} ****"
			def props = configSlurper.parse(req.toURL())
			println "\t # Request Parameters: ${props}"
			
			println "\t # About to send HTTP request..."
			sendRequest(props)

			println "**** END   request: ${req} ****"
		}
	}


	protected void sendRequest(properties) {
		def http = new HTTPBuilder(ENDPOINT_URL)
		def postBody = properties
		http.post(path: HTTP_PATH, body: postBody, requestContentType: URLENC) { resp, reader ->
			println "\t - HTTP Response code: ${resp.statusLine}"
			println "\t >>> HTTP Response Body >>>"
			System.out << reader
			println "\t <<< HTTP Response Body <<<"
		}
	}


	static void main(args) {
		println "HTTP Request Simulator - starting up!"
		new ServletPoster().run()
		println "HTTP Request Simulator - Finished!"
	}
}

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.nio.SelectChannelConnector
import org.eclipse.jetty.webapp.WebAppContext

object JettyLauncher extends App {
  val port = if(System.getenv("PORT") != null) System.getenv("PORT").toInt else 8080
  val server = new Server
  val scc = new SelectChannelConnector
  scc.setPort(port)
  server.setConnectors(Array(scc))

  val context: WebAppContext = new WebAppContext()
  context.setDescriptor("/WEB-INF/web.xml")
  context.setResourceBase("src/main/webapp")
  context.setContextPath("/")
  context.setParentLoaderPriority(true)

  server.setHandler(context)

  server.start()
  server.join()
}
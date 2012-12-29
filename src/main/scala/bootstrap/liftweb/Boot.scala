package bootstrap.liftweb

import net.liftweb.http.LiftRules
import org.tscore.web.lib.api.RestRules


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot() {
    // where to search for snippets
    LiftRules.addToPackages("org/tscore/web/snippet")

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Appending rules for REST API
    LiftRules.dispatch.append(RestRules) // stateless -- no session created
    LiftRules.statelessDispatch.append(RestRules) // stateless -- no session created
  }
}

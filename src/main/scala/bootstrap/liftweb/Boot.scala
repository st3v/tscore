package bootstrap.liftweb

import net.liftweb.http.LiftRules
import org.tscore.web.lib.api.{EndorsementRoutes, ActorRoutes, SubjectRoutes}

class Boot {
  def boot() {
    // where to search for snippets
    LiftRules.addToPackages("org/tscore/web/snippet")

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Appending rules for REST API
    LiftRules.statelessDispatch.append(SubjectRoutes)
    LiftRules.statelessDispatch.append(ActorRoutes)
    LiftRules.statelessDispatch.append(EndorsementRoutes)
  }
}

package org.tscore.graph.repository

import org.springframework.data.neo4j.repository.GraphRepository
import org.tscore.graph.model.Endorsement

trait EndorsementRepository extends GraphRepository[Endorsement] {

}

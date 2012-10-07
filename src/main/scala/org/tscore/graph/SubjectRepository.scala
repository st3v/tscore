package org.tscore.graph

import org.springframework.data.neo4j.repository.GraphRepository

trait SubjectRepository extends GraphRepository[Subject]
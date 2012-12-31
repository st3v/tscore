package org.tscore.trust.service.repository

import org.springframework.data.neo4j.repository.GraphRepository
import org.tscore.trust.model.Subject

trait SubjectRepository extends GraphRepository[Subject]

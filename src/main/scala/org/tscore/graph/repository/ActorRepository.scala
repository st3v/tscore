package org.tscore.graph.repository

import org.springframework.data.neo4j.repository.GraphRepository
import org.tscore.graph.model.Actor
import org.tscore.graph.util.ApplicationContext

trait ActorRepository extends GraphRepository[Actor]

package edu.nju.software.common.util

import io.vertx.ext.web.Route
import io.vertx.ext.web.Router

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
typealias Publish = Router.() -> Route

object Endpoints {
    private val publishes = mutableListOf<Publish>()

    fun publish(publish: Publish):Endpoints {
        publishes.add(publish)
        return this
    }

    fun registerTo(router: Router) {
        publishes.forEach { publish -> router.publish().enable() }
    }
}
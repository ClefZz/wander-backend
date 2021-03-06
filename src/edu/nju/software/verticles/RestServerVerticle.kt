package edu.nju.software.verticles

import edu.nju.software.common.util.Endpoints
import edu.nju.software.controller.initControllers
import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class RestServerVerticle : AbstractVerticle() {
    override fun start() {
        initControllers()

        val router = Router.router(vertx)

        Endpoints.registerTo(router)

        vertx.createHttpServer().requestHandler(router::accept).listen(8080)
    }
}
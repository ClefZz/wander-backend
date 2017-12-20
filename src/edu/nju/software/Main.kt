package edu.nju.software

import edu.nju.software.verticles.RestServerVerticle
import io.vertx.core.Vertx


/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
fun main(args: Array<String>) {
    Vertx.vertx().deployVerticle(RestServerVerticle::class.java.name)
//    publisher
}

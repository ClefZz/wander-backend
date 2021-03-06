package edu.nju.software.controller

import edu.nju.software.common.encodeGeoHash
import edu.nju.software.common.end
import edu.nju.software.common.toPairs
import edu.nju.software.common.uniqueQueryParam
import edu.nju.software.common.util.Endpoints
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.json.Json
import io.vertx.kotlin.core.json.obj

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
internal fun initUserController() {
    Endpoints.publish { get("/hello").produces(JSON_TYPE).handler(::echo) }
            .publish { get("/encode").produces(JSON_TYPE).handler(::encode) }
}

private fun echo(ctx: RoutingContext) {
    ctx.response().end(echo(ctx.queryParams().toPairs()))
}

private fun encode(ctx: RoutingContext) {
    ctx.end(Json.obj("geohash" to encodeGeoHash(ctx.uniqueQueryParam("lat").toDouble(),
            ctx.uniqueQueryParam("lng").toDouble())))
}

private fun echo(map: List<Pair<String, String>>)
        = map.takeIf { it.isNotEmpty() }?.let(Json::obj)?.toString() ?: "Hello"

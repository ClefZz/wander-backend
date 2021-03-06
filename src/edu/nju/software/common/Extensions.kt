package edu.nju.software.common

import io.vertx.core.MultiMap
import io.vertx.ext.web.RoutingContext

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
fun <T, R> Pair<T, T>.map(transform: (T) -> R) = transform(first) to transform(second)

fun MultiMap.toPairs() = map { e -> e.key to e.value }

fun RoutingContext.end(msg: Any) = end(msg.toString())
fun RoutingContext.end(str: String) = response().end(str)
fun RoutingContext.uniqueQueryParam(key: String) = queryParam(key).first()

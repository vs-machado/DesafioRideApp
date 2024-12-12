package com.phoenix.rideapp.feature_ride.domain.util

/**
 * Implementa um debounce nos cliques. Múltiplos cliques registrarão apenas o primeiro
 * clique no intervalo de tempo definido.
 */
fun debounceHandler(onClick: () -> Unit): () -> Unit {
    var latest: Long = 0
    return {
        val now = System.currentTimeMillis()
        if (now - latest >= 1000) {
            onClick()
            latest = now
        }
    }
}
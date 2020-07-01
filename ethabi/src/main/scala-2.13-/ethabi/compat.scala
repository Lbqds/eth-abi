package ethabi

object compat {
  private[ethabi] def mapValues[K, V, R](map: Map[K, V])(f: V => R): Map[K, R] = {
    map.mapValues(f)
  }
}

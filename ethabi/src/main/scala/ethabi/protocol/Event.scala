package ethabi
package protocol

import Response._
import ethabi.types._

final case class Event(indexedValues: Seq[SolType], nonIndexedValues: Seq[SolType]) {
  override def toString: String = {
    s"""
       |{
       |  indexedValues: ${indexedValues.mkString("[", ", ", "]")},
       |  nonIndexedValues: ${nonIndexedValues.mkString("[", ", ", "]")}
       |}
    """.stripMargin
  }
}

object Event {
  def decode(typeInfos: Seq[TypeInfo[_ <: SolType]], log: Log): Event = {
    val topics = log.topics.slice(1, log.topics.length)
    val indexedValues = topics.zip(typeInfos).map {
      case (bytes, typeInfo) =>
        if (typeInfo.isStatic) typeInfo.decode(bytes.value, 0)._1
        else bytes
    }
    val nonIndexedTypeInfo = typeInfos.slice(topics.length, typeInfos.length).headOption
    val nonIndexedValues = nonIndexedTypeInfo.map(_.decode(log.data, 0)._1.asInstanceOf[TupleType].toSeq)
    Event(indexedValues, nonIndexedValues.getOrElse(Seq.empty))
  }
}


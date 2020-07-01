package ethabi
package protocol

import ethabi.types._
import Response._
import ethabi.util._
import ethabi.types.generated.Bytes32

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
    val topics = log.topics.slice(1, log.topics.length).map(Hex.hex2Bytes)
    val data = Hex.hex2Bytes(log.data)
    val indexedValues = topics.zip(typeInfos).map {
      case (bytes, typeInfo) =>
        if (typeInfo.isStatic) typeInfo.decode(bytes, 0)._1
        else Bytes32(bytes)
    }
    val nonIndexedTypeInfo = typeInfos.slice(topics.length, typeInfos.length).headOption
    val nonIndexedValues = nonIndexedTypeInfo.map(_.decode(data, 0)._1.asInstanceOf[TupleType].toSeq)
    Event(indexedValues, nonIndexedValues.getOrElse(Seq.empty))
  }
}


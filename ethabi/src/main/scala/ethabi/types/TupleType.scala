package ethabi.types

import scala.collection.mutable
import generated.Uint256

trait TupleType extends SolType {
  def toSeq: Seq[SolType]
}

object TupleType {
  def encode(typeInfos: Seq[TypeInfo[SolType]], encodedValues: Seq[Array[Byte]]): Array[Byte] = {
    assert(typeInfos.length == encodedValues.length)
    var staticLength, dynamicLength = 0
    typeInfos.zip(encodedValues).foreach {
      case (typeInfo, encoded) =>
        if (typeInfo.isStatic) {
          staticLength += encoded.length
        } else {
          staticLength += 32
          dynamicLength += encoded.length
        }
    }
    val bytes = Array.fill[Byte](staticLength + dynamicLength)(0)
    var staticOffset = 0
    var dynamicOffset = staticLength
    typeInfos.zip(encodedValues).foreach {
      case (typeInfo, encoded) =>
        if (typeInfo.isStatic) {
          Array.copy(encoded, 0, bytes, staticOffset, encoded.length)
          staticOffset += encoded.length
        } else {
          val dynamicOffsetEncoded = TypeInfo[Uint256].encode(Uint256(BigInt(dynamicOffset)))
          Array.copy(dynamicOffsetEncoded, 0, bytes, staticOffset, 32)
          Array.copy(encoded, 0, bytes, dynamicOffset, encoded.length)
          staticOffset += 32
          dynamicOffset += encoded.length
        }
    }
    bytes
  }

  def decode(bytes: Array[Byte], position: Int, typeInfos: Seq[TypeInfo[SolType]]): (Seq[SolType], Int) = {
    var staticOffset, totalConsumed = 0
    val results = new mutable.ListBuffer[SolType]()
    typeInfos.foreach(typeInfo => {
      if (typeInfo.isStatic) {
        val (result, consumed) = typeInfo.decode(bytes, staticOffset + position)
        totalConsumed += consumed
        staticOffset += consumed
        results += result
      } else {
        val (offset, offsetConsumed) = TypeInfo[Uint256].decode(bytes, staticOffset + position)
        staticOffset += offsetConsumed
        totalConsumed += offsetConsumed
        val (result, resultConsumed) = typeInfo.decode(bytes, offset.value.toInt + position)
        totalConsumed += resultConsumed
        results += result
      }
    })
    assert(results.length == typeInfos.length)
    (results.result(), totalConsumed)
  }
}

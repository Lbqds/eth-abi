package ethabi.types

import generated.Uint256
import scala.language.implicitConversions

final class DynamicArray[T <: SolType](val values: List[T]) extends SolType {
  def apply(x: Int): T = values(x)
  override def toString = values.mkString("[", ", ", "]")
}

object DynamicArray {
  def apply[T <: SolType](values: List[T]) = new DynamicArray[T](values)

  implicit def typeInfo[T <: SolType](implicit typeInfoT: TypeInfo[T]): TypeInfo[DynamicArray[T]] = new TypeInfo[DynamicArray[T]] {
    override def name: String = s"${typeInfoT.name}[]"
    override def isStatic: Boolean = false
    override def encode[U >: DynamicArray[T]](value: U): Array[Byte] = {
      val values = value.asInstanceOf[DynamicArray[T]].values
      val encodedLength = Uint256.typeInfo.encode(Uint256(BigInt(values.length)))
      val encodedValues = values.map(typeInfoT.encode(_))
      val typeInfos = List.fill[TypeInfo[T]](values.length)(typeInfoT)
      val bytes = TupleType.encode(typeInfos, encodedValues)
      encodedLength ++ bytes
    }
    override def decode(bytes: Array[Byte], position: Int): (DynamicArray[T], Int) = {
      val (length, lengthConsumed) = Uint256.typeInfo.decode(bytes, position)
      val typeInfos = List.fill[TypeInfo[T]](length.value.toInt)(typeInfoT)
      val (result, consumed) = TupleType.decode(bytes, position + lengthConsumed, typeInfos)
      val values = result.map(_.asInstanceOf[T])
      (DynamicArray(values), consumed + lengthConsumed)
    }
  }
}

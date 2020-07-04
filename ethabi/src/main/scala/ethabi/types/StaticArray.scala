package ethabi.types

// TODO: it would be better if `length` as type argument, maybe `shapeless` can solve this
final class StaticArray[T <: SolType](val values: Seq[T]) extends SolType {
  def apply(x: Int): T = values(x)
  override def toString = values.mkString("[", ", ", "]")
}

object StaticArray {
  def apply[T <: SolType](values: Seq[T]) = new StaticArray[T](values)

  implicit def typeInfo[T <: SolType](implicit typeInfoT: TypeInfo[T], length: Int): TypeInfo[StaticArray[T]] = new TypeInfo[StaticArray[T]] {
    override def name: String =  s"${typeInfoT.name}[$length]"
    override def isStatic: Boolean = typeInfoT.isStatic
    override def encode[U >: StaticArray[T]](value: U): Array[Byte] = {
      val values = value.asInstanceOf[StaticArray[T]].values
      val encodedValues = values.map(typeInfoT.encode(_))
      val typeInfos = List.fill[TypeInfo[T]](values.length)(typeInfoT)
      TupleType.encode(typeInfos, encodedValues)
    }
    override def decode(bytes: Array[Byte], position: Int): (StaticArray[T], Int) = {
      val typeInfos = List.fill[TypeInfo[T]](length)(typeInfoT)
      val (result, consumed) = TupleType.decode(bytes, position, typeInfos)
      val values = result.map(_.asInstanceOf[T])
      (StaticArray[T](values), consumed)
    }
  }
}

/*
 * Copyright (C) 2018-2019 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.grpc.gen.scaladsl

import scala.collection.immutable

import scala.collection.JavaConverters._
import com.google.protobuf.Descriptors._
import scalapb.compiler.{ DescriptorImplicits, GeneratorParams }

/**
 * @param serverPowerApi for the server-side, generate 'power API' classes and methods.
 */
case class Service(packageName: String, name: String, grpcName: String, methods: immutable.Seq[Method], serverPowerApi: Boolean = false) {
  def serializers: Set[Serializer] = (methods.map(_.deserializer) ++ methods.map(_.serializer)).toSet
  def packageDir = packageName.replace('.', '/')
}

object Service {
  def apply(generatorParams: GeneratorParams, fileDesc: FileDescriptor, serviceDescriptor: ServiceDescriptor, serverPowerApi: Boolean): Service = {
    implicit val ops = new DescriptorImplicits(generatorParams, fileDesc.getDependencies.asScala :+ fileDesc)
    import ops._

    val serviceClassName = serviceDescriptor.getName

    Service(
      fileDesc.scalaPackageName,
      serviceClassName,
      fileDesc.getPackage + "." + serviceDescriptor.getName,
      serviceDescriptor.getMethods.asScala.map(method ⇒ Method(method)).to[immutable.Seq],
      serverPowerApi)
  }
}

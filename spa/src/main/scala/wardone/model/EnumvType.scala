/*
 * Copyright 2008 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package wardone
package model

import java.io.Serializable
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

import org.hibernate.HibernateException
import org.hibernate.usertype.UserType
import org.hibernate.engine.spi.SessionImplementor

/**
 * Helper class to translate enum for hibernate
 */
abstract class EnumvType(val et: Enumeration with Enumv) extends UserType {

  val SQL_TYPES = Array({Types.VARCHAR})

  override def sqlTypes = SQL_TYPES

  override def returnedClass = classOf[et.Value]

  override def equals(x: Object, y: Object): Boolean = x == y

  override def hashCode(x: Object) = x.hashCode

  override def nullSafeGet(resultSet: ResultSet, names: Array[String], sessionImplementor: SessionImplementor, owner: Object) : Object = {
    val value = resultSet.getString(names(0))
    if (resultSet.wasNull()) null
    else et.values.find(_.toString == value).getOrElse(null)
  }

  override def nullSafeSet(statement: PreparedStatement, value: Object, index: Int, sessionImplementor: SessionImplementor) {
    if (value == null) {
      statement.setNull(index, Types.VARCHAR)
    } else {
      val en = value.toString
      statement.setString(index, en)
    }
  }

  override def deepCopy(value: Object): Object = value

  override def isMutable = false

  override def disassemble(value: Object) = value.asInstanceOf[Serializable]

  override def assemble(cached: Serializable, owner: Object): Serializable = cached

  override def replace(original: Object, target: Object, owner: Object) = original

}

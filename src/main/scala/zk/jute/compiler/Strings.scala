package zk.jute.compiler

import java.io.File

object Strings {
  def license_notice:String =
    s"""
       |/**
       | * Licensed to the Apache Software Foundation (ASF) under one
       | * or more contributor license agreements.  See the NOTICE file
       | * distributed with this work for additional information
       | * regarding copyright ownership.  The ASF licenses this file
       | * to you under the Apache License, Version 2.0 (the
       | * "License"); you may not use this file except in compliance
       | * with the License.  You may obtain a copy of the License at
       | *
       | *     http://www.apache.org/licenses/LICENSE-2.0
       | *
       | * Unless required by applicable law or agreed to in writing, software
       | * distributed under the License is distributed on an "AS IS" BASIS,
       | * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       | * See the License for the specific language governing permissions and
       | * limitations under the License.
       | */
       |
          """.stripMargin
  def header_protection(name:String, ilist:List[JFile]): String =
    s"""
       |#ifndef __${name.toUpperCase.replace('.', '_')}__
       |#define __${name.toUpperCase.replace('.', '_')}__
    """

  def includes(ilist:List[JFile], ext:String):String =
    s"""
       |${for (i <- ilist) yield s"""#include "${i.getName}.$ext" """}
       |
     """.stripMargin

  def start_defs:String =
    """
      |#ifdef __cplusplus
      |extern "C" {
      |#endif
      |
    """.stripMargin

  def end_defs(name:String):String =
    s"""
      |
      |#ifdef __cplusplus
      |}
      |#endif
      |
      |#endif // ${name.toUpperCase().replace('.', '_')}__
      |      |
    """.stripMargin
}

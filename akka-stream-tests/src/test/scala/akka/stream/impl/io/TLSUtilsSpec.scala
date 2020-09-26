/*
 * Copyright (C) 2020 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.stream.impl.io

import akka.stream.TLSClientAuth
import akka.stream.TLSProtocol.NegotiateNewSession
import javax.net.ssl.{ SSLContext, SSLEngine, SSLParameters }
import org.scalatest.flatspec.AnyFlatSpec

class TLSUtilsSpec extends AnyFlatSpec {

  val sslEngine: SSLEngine = SSLContext.getDefault.createSSLEngine()
  val sslParams: SSLParameters = new SSLParameters()
  val negotiableSession: NegotiateNewSession =
    NegotiateNewSession(None, None, Some(TLSClientAuth.Need), Some(sslParams))

  "SSLParameters" must "not by default require client authentication when it is not passed to the constructor of HttpConnectionContext. In that case its default value is used by akka-http." in {
    assert(!sslParams.getNeedClientAuth)
  }

  "negotiableSession" must "have ClientAuth defined Need" in {
    assert(negotiableSession.clientAuth.contains(TLSClientAuth.Need))
  }

  "SSLEngine" must "have ClientAuth defined Need after calling akka.stream.impl.io.TlsUtils.applySessionParameters with sslEngine and negotiableSession" in {
    TlsUtils.applySessionParameters(sslEngine, negotiableSession)
    assert(sslEngine.getSSLParameters.getNeedClientAuth)
  }

}

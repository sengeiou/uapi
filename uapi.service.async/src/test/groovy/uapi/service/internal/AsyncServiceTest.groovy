/*
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.service.internal

import spock.lang.Specification
import uapi.config.IntervalTime
import uapi.service.async.ICallFailed
import uapi.service.async.ICallSucceed
import uapi.service.async.ICallTimedOut

/**
 * Unit test for AsyncService
 */
class AsyncServiceTest extends Specification {

    def 'Test succeed call'() {
        given:
        AsyncService aSvc = new AsyncService()
        def succeedCallback = Mock(ICallSucceed)
        aSvc.init()

        when:
        aSvc.call({ -> result }, succeedCallback)
        aSvc.destroy()

        then:
        1 * succeedCallback.accept(callId, result)

        where:
        callId  | result
        '1'     | 'Test'
    }

    def 'Test succeed call without result'() {
        given:
        AsyncService aSvc = new AsyncService()
        aSvc.init()

        when:
        aSvc.call({ -> })
        aSvc.destroy()

        then:
        noExceptionThrown();
        aSvc.callCount() == 0
    }

    def 'Test failed call'() {
        given:
        AsyncService aSvc = new AsyncService()
        def succeedCallback = Mock(ICallSucceed)
        def failedCallback = Mock(ICallFailed)
        aSvc.init();

        when:
        aSvc.call({ -> throw ex }, succeedCallback, failedCallback)
        aSvc.destroy()

        then:
        0 * succeedCallback.accept(_ as String, _ as Object)
        1 * failedCallback.accept(callId, ex)
        aSvc.callCount() == callCount

        where:
        callId  | ex                | callCount
        '1'     | new Exception()   | 0
    }

    def 'Test failed call without callback'() {
        given:
        AsyncService aSvc = new AsyncService()
        aSvc.init()

        when:
        aSvc.call({ -> throw ex }, null as ICallSucceed, null as ICallFailed)
        aSvc.destroy()

        then:
        aSvc.callCount() == callCount

        where:
        callId  | ex                | callCount
        '1'     | new Exception()   | 0
    }

    def 'Test time out call'() {
        given:
        AsyncService aSvc = new AsyncService()
        def succeedCallback = Mock(ICallSucceed)
        def failedCallback = Mock(ICallFailed)
        def timedOutCallback = Mock(ICallTimedOut)
        def expiredTime = Mock(IntervalTime) {
            milliseconds() >> expTime
        }
        def options = ['TimeOut' : expiredTime]
        aSvc._timeOfCheck = Mock(IntervalTime) {
            milliseconds() >> checkTime
        }
        aSvc.init()

        when:
        aSvc.call({ -> Thread.currentThread().sleep(200) }, succeedCallback, failedCallback, timedOutCallback, options)
        Thread.currentThread().sleep(300)
        aSvc.destroy()

        then:
        0 * succeedCallback.accept(_ as String, _ as Object)
        0 * failedCallback.accept(_ as String, _ as Throwable)
        1 * timedOutCallback.accept(callId)
        aSvc.callCount() == callCount

        where:
        callId  | expTime   | checkTime | callCount
        '1'     | 10        | 20        | 0
    }

    def 'Test time out call without callback'() {
        given:
        AsyncService aSvc = new AsyncService()
        def expiredTime = Mock(IntervalTime) {
            milliseconds() >> expTime
        }
        def options = ['TimeOut' : expiredTime]
        aSvc._timeOfCheck = Mock(IntervalTime) {
            milliseconds() >> checkTime
        }
        aSvc.init()

        when:
        aSvc.call({ -> Thread.currentThread().sleep(200) }, null as ICallSucceed, null as ICallFailed, null as ICallTimedOut, options)
        aSvc.destroy()

        then:
        aSvc.callCount() == callCount

        where:
        callId  | expTime   | checkTime | callCount
        '1'     | 10        | 20        | 0
    }
}

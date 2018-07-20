package com.qiscus.sdk.data.remote;

import android.accounts.Account;

import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.data.model.QiscusAccount;
import com.qiscus.sdk.data.model.QiscusNonce;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Date;

import rx.Observable;

/**
 * @author Yuana andhikayuana@gmail.com
 * @since Jul, Tue 17 2018 13.38
 **/
@RunWith(RobolectricTestRunner.class)
public class QiscusApiTest {

    private QiscusApi mockApi;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Qiscus.init(RuntimeEnvironment.application, "dragongo");

        mockApi = Mockito.mock(QiscusApi.class);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void whenRequestNonceShouldSuccess() {

        Observable<QiscusNonce> nonce = Observable.just(new QiscusNonce(new Date(), "nonce"));

        Mockito.when(mockApi.requestNonce()).thenReturn(nonce);
    }

    @Test
    public void whenLoginShouldSuccess() {

        Observable<QiscusAccount> returnAccount = Observable.just(new QiscusAccount());

        Mockito.when(mockApi.login("token")).thenReturn(returnAccount);

    }

}
package com.peaceful.apm.alert;

import com.google.common.base.Throwables;
import com.google.inject.AbstractModule;
import com.peaceful.apm.alert.msgbox.MsgBoxConfig;
import com.peaceful.apm.alert.msgbox.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WangJun
 * @version 1.0 16/6/19
 */
public class AlertModule extends AbstractModule {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void configure() {
        try {
            bind(MsgQueue.class).to(LocalMsgQueue.class).asEagerSingleton();

            logger.debug("notice msg impl -> {}", MsgBoxConfig.msgNoticeImpl);
            bind(MsgNotice.class).to((Class<? extends MsgNotice>) AlertModule.class.forName(MsgBoxConfig.msgNoticeImpl));

        } catch (ClassNotFoundException e) {
            Throwables.propagate(e);
        }

    }
}

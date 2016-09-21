package com.peaceful.apm.alert;

import com.google.common.base.Throwables;
import com.google.inject.AbstractModule;
import com.peaceful.apm.alert.expression.JEXLTermService;
import com.peaceful.apm.alert.impl.NopMsgNotice;
import com.peaceful.apm.alert.msgbox.LocalMsgBox;
import com.peaceful.apm.alert.msgbox.MsgBox;
import com.peaceful.apm.alert.msgbox.MsgHandler;
import com.peaceful.boot.Application;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * alert module
 *
 * @author WangJun
 * @version 1.0 16/6/19
 */
public class AlertModule extends AbstractModule {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void configure() {
        // msg box
        bind(MsgBox.class).to(LocalMsgBox.class).asEagerSingleton();
        // msg send handler
        try {
            String msgNotice = Application.getConfigContext().getString("apm.msg.notice.impl");
            bind(MsgHandler.class).to((Class<? extends MsgHandler>) Class.forName(msgNotice)).asEagerSingleton();
        } catch (ClassNotFoundException e) {
            Throwables.propagate(e);
        }
        // expression parse
        bind(TermService.class).to(JEXLTermService.class).asEagerSingleton();
    }
}

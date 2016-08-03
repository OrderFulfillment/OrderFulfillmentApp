package ofa.mobile.bean;

import ofa.mobile.constants.OfaConstants;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;

public class ResetDataBean {
    public ResetDataBean() {
    }

    public void backToHomeFromSoExcp() {
        AdfmfJavaUtilities.setELValue("#{pageFlowScope.backFrom}", OfaConstants.BACK_FROM_ORDERS_EXCEPTION);
    }
}

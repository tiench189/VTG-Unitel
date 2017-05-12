package com.vtg.app.util;

public interface CommonDefine {
	public static final String USERNAME = "2427adb509b93ed4";
	public static final String PASSWORD = "e7a34fb48b5f12af4687ccba21e07165";

	public static final String MY_PACKAGE = "com.vtg.unitel";

	public static final int DEBIT = 1;
	public static final int CREDIT = 0;

	public static final String TYPE_3G = "1";
	public static final String TYPE_4G = "2";
	public static final String TYPE_LT_3G = "3";
	public static final String TYPE_LT_4G = "4";

	public static final String METHOD_POST = "post";
	public static final String METHOD_GET = "get";

	public static final String NUMBER_HEADER = "856";

	public static final class PreferenceKey {
		public static final String LOCATE = "locate";
		public static final String PHONE_NUMBER = "phone_number";
		public static final String PHONE_NUMBER_FULL = "phone_number_full";
		public static final String SAVE_LOGIN = "save_login";
		public static final String FIRST_USE = "first_use";
	}

	public static final class mXML {
		public static final String RESULT = "return";
		public static final String CODE = "code";
		public static final String MESSAGE = "message";
		public static final String ACT_STATUS = "actStatus";
		public static final String ACT_STATUS_BIT = "actStatusBit";
		public static final String BUS_TYPE = "busType";
		public static final String CUST_ID = "custId";
		public static final String IMSI = "imsi";
		public static final String ISDN = "isdn";
		public static final String PRODUCT_CODE = "productCode";
		public static final String SERIAL = "serial";
		public static final String SERVICE_TYPE = "serviceType";
		public static final String START_DATE_TIME = "startDatetime";
		public static final String STATUS = "status";
		public static final String SUB_ID = "subId";
		public static final String TEL_SERVICE_ID = "telServiceId";
		public static final String TELECOM_SERVICE = "telecomService";
		public static final String ERR_CODE = "errCode";
		public static final String DESCRIPTION = "description";
		public static final String ERR_OCS = "errOcs";
		public static final String RESPONSE_CODE = "responseCode";
		public static final String VALUE = "value";
		public static final String USER_USING = "userUsing";
		public static final String ADDRESS = "address";
		public static final String BIRTHDATE = "birthdate";
		public static final String SUB_TYPE = "subType";
		public static final String ERROR = "error";
	}

	public static final class SoapTag {
		public static final String USERNAME = "username";
		public static final String PASSWORD = "password";
		public static final String RAWDATA = "rawData";
		public static final String NAME = "name";
		public static final String VALUE = "value";
	}

	public static final class WSCode {
		public static final String GET_SUB_INFO = "getsubinfo";
		public static final String GET_SUB_ACC = "getSubAcc";
		public static final String TOP_UP_PAY_BY_CARD = "topUpPayByCard";
		public static final String CHECK_3G = "check3G";
		public static final String REGISTER_MI = "registerMI";
		public static final String REGISTER_MI_NEW = "RegisterDataNew";
		public static final String REGISTER_4G = "register4G";
		public static final String REGISTER_LT = "RegisterLT";
		public static final String REGISTER_LT4G = "RegisterLT4G";
		public static final String REGISTER_LT_NEW = "RegisterLTNew";
		public static final String REMOVE_MI = "remove3G";
		public static final String REMOVE_MI_NEW = "RemoveDataNew";
		public static final String REMOVE_4G = "remove4G";
		public static final String BUY_DATA = "buyDataUnlimited";
		public static final String GET_DEBIT_INFO = "GetDebitInfoInCycle";
		public static final String PAY_BY_CARD = "payByCard";
		public static final String GET_DEBIT_INFO_BUILD = "SCgetDetailInfoBill";
	}

	public static final class Action {
		public static final String ACTION = "action";
		public static final String FILTER = "com.vtg.app";
		public static final String RELOAD_ACCOUNT = "reload_account";
		public static final String DONE_RELOAD_ACCOUNT = "done_reload_account";
		public static final String DONE_RELOAD_DATA = "done_reload_data";
		public static final String RELOAD_DATA = "reload_data";
		public static final String REGISTER_SUCCESS = "register_success";
		public static final String LOAD_PROFILE = "load_profile";
		public static final String CHANGE_TAB = "change_tab";
		public static final String LOGOUT = "logout";
	}

	public static final class MyService {
		public static final String DOMAIN = "http://apimf.truonglx.me/";
		public static final String REGIS = DOMAIN + "api/register";
		public static final String CHANGE_PASS = DOMAIN + "api/changepassword";
		public static final String RESET = DOMAIN + "api/sendpass";
		public static final String LOGIN = DOMAIN + "api/login";
		public static final String PROMOTION = DOMAIN
				+ "api/promote/list?signature=dcb0f814d2d18359cc6e77f63e890806";
		public static final String VAS = DOMAIN
				+ "api/vas/list?signature=dcb0f814d2d18359cc6e77f63e890806";
		public static final String BANNER = DOMAIN
				+ "api/banner/list?signature=dcb0f814d2d18359cc6e77f63e890806";
		public static final String ABOUT = DOMAIN
				+ "api/info/about?signature=dcb0f814d2d18359cc6e77f63e890806";
		public static final String TARIFF = DOMAIN
				+ "api/info/tariff?signature=dcb0f814d2d18359cc6e77f63e890806";
		public static final String SHOWROOM = DOMAIN
				+ "api/showroom/listbyprovince?signature=dcb0f814d2d18359cc6e77f63e890806";
		public static final String GET_SUB_DATA = DOMAIN
				+ "api/3g/list?signature=dcb0f814d2d18359cc6e77f63e890806";
		public static final String GET_TIME_DATA = DOMAIN
				+ "api/3g/typetime/list?signature=dcb0f814d2d18359cc6e77f63e890806";
		public static final String GET_EXTEND_DATA = DOMAIN
				+ "api/packextent/list";
		public static final String DETAIL_3G = DOMAIN
				+ "api/3g/item";
		public static final String VERSION = DOMAIN
				+ "api/version/android?signature=dcb0f814d2d18359cc6e77f63e890806";
	}

	public static final String SIGNATURE = "signature";

	public static final class TabIndex {
		public static final int HOME = 0;
		public static final int DEBIT = 1;
		public static final int DATA = 2;
		public static final int OFFER = 3;
		public static final int SERVICE = 4;
		public static final int CREDIT = 5;
		public static final int MORE = 6;
	}

}

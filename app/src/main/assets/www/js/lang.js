var EN = {
	menu : {
		setting : "SETTING",
		dashboard : "DASHBOARD",
		information : "INFORMATION"
	},
	setting : {
		header : "SMART GATEWAY SETTING",
		title : "SET UP CONNECTION",
		caption : "The smart gateway needs to connect with our server by WIFI network to send and receive the data. Please set up the gateway connection by choosing the WIFI name and password (if need) of the WIFI.",
		ssid : "WIFI NAME",
		password : "WIFI PASSWORD",
		showPassword : "SHOW PASSWORD",
		cmdScan : "SCAN",
		cmdConnect : "CONNECT",
		cmdRebootDevice : "REBOOT GATEWAY"
	},
	dashboard : {
		header : "DEVICE CONTROL CENTER",
		deviceName : {
			soilSensor : "SOIL SENSOR",
			airSensor : "AIR SENSOR",
			lightSensor : "LIGHT SENSOR",
			smartTank : "HYDROPONIC CONTROLLER"
		},
		sensorName : {
			lightSensor : "LIGHT",
			airTemp : "AIR TEMPERATURE",
			airHumidity : "AIR HUMIDITY",
			soilTemp : "SOIL TEMPERATURE",
			soilHumidity : "SOIL HUMIDITY",
			soilEC : "SOIL EC",
			soilPH : "SOIL PH",
			waterTemp : "WATER TEMPERATURE",
			waterPH : "WATER PH",
			waterEC : "WATER EC",
			waterORP : "WATER ORP",
			waterLevel : "WATER LEVEL",
			waterLeak : "WATER LEAK",
			error : "ERROR",
			bat : "BATTERY",
			co2 : "CO2"
		},
		controllerName : {
			lamp : "LAMP",
			waterPump : "WATER PUMP",
			oxygenPump : "OXYGEN PUMP",
			fan : "FAN",
			mistingPump : "MISTING PUMP",
			ac : "AIR CONDITIONER",
			waterValveIn : "WATER VALVE INPUT",
			waterValveOut : "WATER VALVE OUTPUT",
			washingTank : "WASHING TANK"
		},
		irLearningSuccessfully : "IR LEARNING SUCCESSFULLY",
		irLearningFailed : "IR LEARNING FAILED. PLEASE TRY AGAIN"
	},
	information : {
		header : "SMART GATEWAY PROFILE",
		deviceInfoTitle : "DEVICE INFORMATION",
		deviceInfoCaption : "The smart gateway is researched and developed by AGRHUB CO.,LTD. The device is managed by UUID",
		deviceName : "DEVICE NAME",
		deviceMacAddress : "DEVICE UUID",
		deviceManufacture : "MANUFACTURE",
		deviceProductionDate : "PRODUCTION DATE",
		deviceHardwareID : "HARDWARE ID",
		deviceUpdateTitle : "FIRWARE INFORMATION",
		deviceUpdateCaption : "The gateway can automatic update firmware to improve performance and support new devices. User can choose update new firmware automaticaly or by manually",
		deviceUpdateWarning : "Warning: The updating can effect to device. Device can not work with wrong firmware. Please only use auto update feature.",
		deviceOnlineUpdate : "ONLINE UPDATE",
		deviceSoftwareVersion : "SOFTWARE VERSION",
		deviceSoftwareDate : "RELEASE DATE",
		deviceSoftwareNewVersion : "NEW VERSION",
		cmdFirmwareCheck : "CHECK NEW FIRMWARE",
		cmdUpdate : "UPDATE",
		toastDeviceNewVersion : "HAS NEW VERSION. PLEASE UPDATE YOUR DEVICE",
		toastDeviceUpToDate : "YOUR DEVICE IS UP TO DATE",
		deviceLocalUpdate : "LOCAL UPDATE",
		devicePasswordUpdate : "PASSWORD",
		deviceFirmwarePath : "FIRMWARE PATH",
		cmdFactoryReset : "FACTORY RESET"
	},
	actions : {
		chooseWiFi : "CHOOSE WIFI NAME",
		emptySSID : "PLEASE CHOOSE A WIFI NETWORK",
		connectSuccessfully : "CONNECT SUCCESSFULLY",
		connectFailed : "CONNECT FAILED. WRONG PASSWORD OR NETWORK ERROR",
		deviceRestarted : "DEVICE WILL BE RESTARTED IN 10s",
		emptyPassword : "PASSWORD IS EMPTY",
		emptyFirmware : "PLEASE PICK UP A FIRMWARE FILE",
		updateSuccessfully : "UPDATE SUCCESSFULLY. DEVICE WILL BE RESTARTED IN 10S",
		updateFailed : "UPDATE FAILED, PLEASE TRY AGAIN LATER",
		checkFirmwareFailed : "CHECK FIRMWARE FAILED. PLEASE TRY AGAIN LATER",
		deviceOutOfDate : "DEVICE HAS A NEW FIRMWARE VERSION",
		deviceUpToDate : "DEVICE IS UP TO DATE",
		factoryResetSuccessfully : "DEVICE HAS BEEN RESET TO FACTORY. DEVICE WILL BE RESTARTED IN 10S",
		factoryResetFailed : "FACTORY RESET FAILED. PLEASE TRY AGAIN LATER."
	}
};

var VI = {
	menu : {
		setting : "CÀI ĐẶT",
		dashboard : "QUẢN LÝ",
		information : "THÔNG TIN"
	},
	setting : {
		header : "CÀI ĐẶT BỘ ĐIỀU KHIỂN TRUNG TÂM",
		title : "CẤU HÌNH KẾT NỐI INTERNET",
		caption : "Bộ điều khiển trung tâm cần phải được kết nối internet đến máy chủ để gởi và nhận dữ liệu. Vui lòng cấu hình kết nối cho bộ điều khiển trung tâm bằng cách chọn mạng WIFI và mật khẩu tương ứng với mạng WIFI.",
		ssid : "TÊN MẠNG WIFI",
		password : "MẬT KHẨU WIFI",
		showPassword : "HIỆN  MẬT KHẨU",
		cmdScan : "QUÉT MẠNG WIFI",
		cmdConnect : "KẾT NỐI",
		cmdRebootDevice : "KHỞI ĐỘNG LẠI"
	},
	dashboard : {
		header : "QUẢN LÝ THIẾT BỊ ĐƯỢC KẾT NỐI VỚI BỘ ĐIỀU KHIỂN TRUNG TÂM",
		deviceName : {
			soilSensor : "CẢM BIẾN ĐẤT",
			airSensor : "CẢM BIẾN KHÔNG KHÍ",
			lightSensor : "CẢM BIẾN ÁNH SÁNG",
			smartTank : "BỘ ĐIỀU KHIỂN THỦY CANH"
		},
		sensorName : {
			light : "ÁNH SÁNG",
			airTemp : "NHIỆT ĐỘ",
			airHumidity : "ĐỘ ẨM",
			soilTemp : "NHIỆT ĐỘ ĐẤT",
			soilHumidity : "ĐỘ ẨM ĐẤT",
			soilEC : "DINH DƯỠNG ĐẤT",
			soilPH : "PH ĐẤT",
			waterTemp : "NHIỆT ĐỘ NƯỚC",
			waterPH : "PH NƯỚC",
			waterEC : "EC NƯỚC",
			waterORP : "ORP NƯỚC",
			waterLevel : "MỰC NƯỚC",
			waterLeak : "RÒ RỈ NƯỚC",
			error : "LỖI",
			bat : "PIN",
			co2 : "CO2"
		},
		controllerName : {
			lamp : "ĐÈN",
			waterPump : "BƠM NƯỚC",
			oxygenPump : "BƠM OXY",
			fan : "QUẠT",
			mistingPump : "PHUN SƯƠNG",
			ac : "ĐIỀU HÒA",
			waterValveIn : "VAN CẤP NƯỚC",
			waterValveOut : "VAN XẢ NƯỚC",
			washingTank : "VỆ SINH BỒN"
		},
		irLearningSuccessfully : "HỌC LỆNH THÀNH CÔNG",
		irLearningFailed : "HỌC LỆNH KHÔNG THÀNH CÔNG. VUI LÒNG THỬ LẠI"
	},
	information : {
		header : "THÔNG TIN THIẾT BỊ ĐIỀU KHIỂN TRUNG TÂM",
		deviceInfoTitle : "THÔNG TIN",
		deviceInfoCaption : "Thiết bị điều khiển trung tâm được nghiên cứu và phát triển bởi AGRHUB CO.,LTD. Mỗi thiết bị sẽ có một mã số duy nhất để quản lý",
		deviceName : "TÊN THIẾT BỊ",
		deviceMacAddress : "MÃ SỐ THIẾT BỊ",
		deviceManufacture : "NHÀ SẢN XUẤT",
		deviceProductionDate : "NGÀY SẢN XUẤT",
		deviceHardwareID : "MÃ PHẦN CỨNG",
		deviceUpdateTitle : "THÔNG TIN PHẦN MỀM",
		deviceUpdateCaption : "Thiết bị có khả năng tự cập nhật phần mềm để nâng cáo hiệu năng và bổ sung các thiết bị ngoại vi mới. Người dùng có thể cập nhật phần mềm cho thiết bị trực tuyến hoặc thủ công",
		deviceUpdateWarning : "Cảnh báo: Việc cập nhật sẽ ảnh hưởng đến thiết bị và có thể ngừng hoạt động nếu cập nhật sai phần mềm. Chỉ nên sử dụng chức cập nhật trực tuyến để đảm bảo an toàn cho thiết bị.",
		deviceOnlineUpdate : "TRỰC TUYẾN",
		deviceSoftwareVersion : "PHIÊN BẢN PHẦN MỀM",
		deviceSoftwareDate : "NGÀY PHÁT HÀNH",
		deviceSoftwareNewVersion : "PHIÊN BẢN MỚI",
		cmdFirmwareCheck : "KIỂM TRA CẬP NHẬT",
		cmdUpdate : "CẬP NHẬT",
		toastDeviceNewVersion : "THIẾT BỊ CÓ PHIÊN BẢN MỚI",
		toastDeviceUpToDate : "THIẾT BỊ HIỆN TẠI ĐÃ CHẠY PHIÊN BẢN MỚI NHẤT",
		deviceLocalUpdate : "NGOẠI TUYẾN",
		devicePasswordUpdate : "MẬT KHẨU BẢO VỆ",
		deviceFirmwarePath : "ĐƯỜNG DẪN FILE CẬP NHẬT",
		cmdFactoryReset : "KHÔI PHỤC CÀI ĐẶT GỐC"
	},
	actions : {
		chooseWiFi : "CHỌN MẠNG WIFI",
		emptySSID : "VUI LÒNG CHỌN MẠNG WIFI ĐỂ KẾT NỐI INTERNET",
		connectSuccessfully : "KẾT NỐI THÀNH CÔNG",
		connectFailed : "KẾT NỐI KHÔNG THÀNH CÔNG. LỖI SAI MẬT KHẨU HOẶC MẠNG WIFI BỊ LỖI",
		deviceRestarted : "THIẾT BỊ SẼ KHỞI ĐỘNG LẠI TRONG 10S",
		emptyPassword : "CHƯA NHẬP MẬT KHẨU QUẢN TRỊ",
		emptyFirmware : "VUI LÒNG CHỌN FIRMWARE CỦA THIẾT BỊ",
		updateSuccessfully : "CẬP NHẬT THÀNH CÔNG. THIẾT BỊ SẼ KHỞI ĐỘNG LẠI TRONG 10S",
		updateFailed : "LỖI CẬP NHẬT. VUI LÒNG THỬ LẠI SAU",
		checkFirmwareFailed : "LỖI KIỂM TRA FIRMWARE. VUI LÒNG THỬ LẠI SAU",
		deviceOutOfDate : "THIẾT BỊ CÓ PHẦN MỀM MỚI",
		deviceUpToDate : "THIẾT BỊ ĐANG CHẠY PHẦN MỀM MỚI NHẤT",
		factoryResetSuccessfully : "KHÔI PHỤC CÀI ĐẶT GỐC THÀNH CÔNG. THIẾT BỊ SẼ KHỞI ĐỘNG LẠI TRONG 10S",
		factoryResetFailed : "LỖI KHÔI PHỤC CÀI ĐẶT GỐC. VUI LÒNG THỬ LẠI SAU."
	}
};

var CN = {

};

var JP = {

};

var LANG = VI;

function Language(){
	var fn = this;
	fn.setLanguage = function(lang){
		if(lang == "vi"){
			LANG = VI;
		}

		if(lang == "en"){
			LANG = EN;
		}

		if(lang == "cn"){
			LANG = CN;
		}

		if(lang == "jp"){
			LANG = JP;
		}
	};

	fn.init = function(){
		fn.initMenu();
		fn.initSettingPage();
		fn.initDashboardPage();
		fn.initProfilePage();
	};

	fn.initMenu = function(){
		$("a[href='#sta-setting-page']").html(LANG["menu"]["setting"]);
		$("a[href='#dashboard-page']").html(LANG["menu"]["dashboard"]);
		$("a[href='#device-info-page']").html(LANG["menu"]["information"]);
	};

	fn.initSettingPage = function(){
		$("#sta-setting-page .header").html(LANG["setting"]["header"]);
		$("#sta-setting-page .card-title").html(LANG["setting"]["title"]);
		$("#sta-setting-page .card-caption").html(LANG["setting"]["caption"]);
		$("#sta-setting-page label[for='staSSID']").html(LANG["setting"]["ssid"]);
		$("#sta-setting-page label[for='staPWD']").html(LANG["setting"]["password"]);
		$("#sta-setting-page label[for='cmdShowPWD']").html(LANG["setting"]["cmdShowPWD"]);
		$("#cmdScanWiFi").html(LANG["setting"]["cmdScan"]);
		$("#cmdConnectWiFi").html(LANG["setting"]["cmdConnect"]);
		$("#cmdResetDevice").html(LANG["setting"]["cmdRebootDevice"]);
	};

	fn.initDashboardPage = function(){
		$("#dashboard-page .header").html(LANG["dashboard"]["header"]);
		//$("#dashboard-page .").html(LANG["dashboard"]["header"]);
	};

	fn.initProfilePage =  function(){
		$("#device-info-page .header").html(LANG["information"]["header"]);

		$("#device-info-container .card-title").html(LANG["information"]["deviceInfoTitle"]);
		$("#device-info-container p").html(LANG["information"]["deviceInfoCaption"]);

		$("#device-info-container .device-name").html(LANG["information"]["deviceName"]);
		$("#device-info-container .device-mac-address").html(LANG["information"]["deviceMacAddress"]);
		$("#device-info-container .device-manufacture").html(LANG["information"]["deviceManufacture"]);
		$("#device-info-container .device-production-date").html(LANG["information"]["deviceProductionDate"]);
		$("#device-info-container .device-hardware-version").html(LANG["information"]["deviceHardwareID"]);

		$("#device-software-container .card-title").html(LANG["information"]["deviceUpdateTitle"]);
		$("#device-software-container .card-caption").html(LANG["information"]["deviceUpdateCaption"]);
		$("#device-software-container .card-note").html(LANG["information"]["deviceUpdateWarning"]);

		$("#device-software-container a[href='#onlineOTAUpdate']").html(LANG["information"]["deviceOnlineUpdate"]);
		$("#device-software-container a[href='#localOTAUpdate']").html(LANG["information"]["deviceLocalUpdate"]);
		
		$("#device-software-container .software-version").html(LANG["information"]["deviceSoftwareVersion"]);
		$("#device-software-container .software-release-date").html(LANG["information"]["deviceSoftwareDate"]);
		$("#device-software-container .new-software-version").html(LANG["information"]["deviceSoftwareNewVersion"]);
		$("#cmdCheckUpdateFirmware").html(LANG["information"]["cmdFirmwareCheck"]);	
		$("#cmdOnlineUpdateFirmware").html(LANG["information"]["cmdUpdate"]);	
		
		$("#device-software-container label[for='adminPWD']").html(LANG["information"]["devicePasswordUpdate"]);	
		$("#device-software-container label[for='firmwarePath']").html(LANG["information"]["deviceFirmwarePath"]);	
		$("#cmdLocalUpdateFirmware").html(LANG["information"]["cmdUpdate"]);	

		$("#cmdFactoryReset").html(LANG["information"]["cmdFactoryReset"]);
	};
}
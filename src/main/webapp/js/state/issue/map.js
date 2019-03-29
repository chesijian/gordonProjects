/**
 * 
 */
//初始化高度
//document.getElementById("main").style.height = (parent.document.documentElement.clientHeight-18)+"px";
var timeArr = [ '00:15', '00:30', '00:45', '01:00', '01:15',
						'01:30', '01:45', '02:00', '02:15', '02:30',
						'02:45', '03:00', '03:15', '03:30', '03:45',
						'04:00', '04:15', '04:30', '04:45', '05:00',
						'05:15', '05:30', '05:45', '06:00', '06:15',
						'06:30', '06:45', '07:00', '07:15', '07:30',
						'07:45', '08:00', '08:15', '08:30', '08:45',
						'09:00', '09:15', '09:30', '09:45', '10:00',
						'10:15', '10:30', '10:45', '11:00', '11:15',
						'11:30', '11:45', '12:00', '12:15', '12:30',
						'12:45', '13:00', '13:15', '13:30', '13:45',
						'14:00', '14:15', '14:30', '14:45', '15:00',
						'15:15', '15:30', '15:45', '16:00', '16:15',
						'16:30', '16:45', '17:00', '17:15', '17:30',
						'17:45', '18:00', '18:15', '18:30', '18:45',
						'19:00', '19:15', '19:30', '19:45', '20:00',
						'20:15', '20:30', '20:45', '21:00', '21:15',
						'21:30', '21:45', '22:00', '22:15', '22:30',
						'22:45', '23:00', '23:15', '23:30', '23:45',
						'24:00' ];
Array.prototype.insert = function (index, item) {
this.splice(index, 0, item);
};
require.config({
    paths: {
        echarts: './../js/echart-2.2.7/dist'
    }
});
require(
[
    'echarts',
    'echarts/chart/map'   // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
    
],



function (ec) {
	
	//alert(document.getElementById("main").style.height);
    var myChart = ec.init(document.getElementById('main'));
    
    myChart.showLoading({
	    text: '正在努力的读取数据中...',    //loading话术
	});
	var optionsArr = new Array();
	if(data != null){
		var i = 0;
		//console.info(data);
		for(i=1;i<97;i++){
			makeLineData = data[i];
			var optionObj = null;
			//如果是第一个
			if(i == 1){
				if(makeLineData == undefined){
					//alert("对不起，当天没有数据！");
				}
			optionObj = {
    	    backgroundColor: '#1b1b1b',
    	    color: ['gold','aqua','lime'],
    	    title : {
    	    	'text':timeArr[0]+'时段各通道电力信息',
    	        //subtext:'数据纯属虚构',
    	        x:'center',
    	        textStyle : {
    	            color: '#fff'
    	        }
    	    },
    	    toolbox: {
    	        show : true,
    	        orient : 'vertical',
    	        x: 'right',
    	        y: 'center',
    	        feature : {
    	            mark : {show: true},
    	            dataView : {show: true, readOnly: false},
    	            restore : {show: true},
    	            saveAsImage : {show: true}
    	        }
    	    },
    	    dataRange: {
    	        min : 0,
    	        max : 100,
    	        calculable : true,
    	        color: ['#ff3333', 'orange', 'yellow','lime','aqua','#1b1b1b'],
    	        textStyle:{
    	            color:'#fff'
    	        }
    	    },
    	    tooltip : {
    	        trigger: 'item',
    	        formatter: function(params,ticket,callback){
    	        	return formatterStr(params,1);
    	        }
    	    },
    	    animationDurationUpdate: 1000, // for update animation, like legend selected.
    	    series : [
    	        {
    	            name: '成都',
    	            type: 'map',
    	            roam: true,
    	            hoverable: false,
    	            mapType: 'china',
    	            itemStyle:{
    	            	normal:{
    	            		label:{show:true},
    	                    borderColor:'rgba(100,149,237,1)',
    	                    borderWidth:0.5,
    	                    areaStyle:{
    	                        color: '#1b1b1b'
    	                    }
    	                }//,
						//emphasis:{label:{show:true}}
    	            },
    	            data:[],
    	            geoCoord: {
    	            	'上海': [121.4648,31.2891],
    	                '新疆': [87.9236,43.5883],
    	                '甘肃': [103.5901,36.3043],
    	                '北京': [116.4551,40.2539],
    	                '江苏': [118.8062,31.9208],
    	                '江西': [116.0046,28.6633],
    	                '福建': [118.1689,24.6478],
    	                '安徽': [117.29,32.0581],
    	                '内蒙': [111.4124,40.4901],
    	                '黑龙江': [127.9688,45.368],
    	                '天津': [117.4219,39.4189],
    	                '山西': [112.3352,37.9413],
    	                '四川': [103.9526,30.7617],
    	                '西藏': [91.1865,30.1465],
    	                '浙江': [119.5313,29.8773],
    	                '湖北': [114.3896,30.6628],
    	                '辽宁': [123.1238,42.1216],
    	                '山东': [117.1582,36.8701],
    	                '海南': [110.3893,19.8516],
    	                '河北': [114.4995,38.1006],
    	                '青海': [101.4038,36.8207],
    	                '陕西': [109.1162,34.2004],
    	                '河南': [113.4668,34.6234],
    	                '重庆': [107.7539,30.1904],
    	                '宁夏': [106.3586,38.1775],
    	                '吉林': [125.8154,44.2584],
    	                '湖南': [113.0823,28.2568]
    	            },
    	            markLine : {
    	                smooth:true,
    	                effect : {
    	                    show: true,
    	                    scaleSize: 1,
    	                    period: 30,
    	                    color: '#fff',
    	                    shadowBlur: 10
    	                },
    	                
    	                itemStyle : {
    	                    normal: {
    	                    	
    	                        borderWidth:1,
    	                        label: {
    	                            show: true,
    	                            formatter: function (params,ticket,callback) {
    	                                //console.log(params);
    	                                
    	                                return formatterStr(params,0);
    	                            }
    	                        },
    	                        lineStyle: {
    	                            type: 'solid',
    	                            shadowBlur: 10
    	                        }
    	                    }
    	                },
    	                data : makeLineData
    	            },
    	            markPoint : {
    	                symbol:'emptyCircle',
    	                symbolSize : function (v){
    	                    return 10 + v/10;
    	                },
    	                effect : {
    	                    show: true,
    	                    shadowBlur : 0
    	                },
    	                itemStyle:{
    	                    normal:{
    	                        label:{show:false}
    	                    }
    	                },
    	                data : areaArry
    	            }
    	        }
    	        
    	    ]};
			}else{
				optionObj = {
						 title : {'text':timeArr[i-1]+'时段各通道电力信息'},
			                series : [
			                    {
			                    	markLine : {
			        	                
			        	                data :makeLineData
			                    	}
			                    }
			                    ]
				};
			}
			
			optionsArr.push(optionObj);
		}
	}
	//console.info(optionsArr);
    option = {
    		timeline:{
    			type: 'number',
    	        data : timeArr,//[0,1,2,3,4],
    	        label : {
    	        	show:true,
    	        	formatter: function(v){
    	                //return '00:'+ (v > 10 ? v : ('0' + v))
    	                //return timeArr[v];
    	                return v;
    	            }
    	        },
    	        autoPlay : true,
    	        playInterval : 2500
    	    },
    		options:optionsArr
    	};
    	                    
    myChart.setOption(option);
    myChart.hideLoading();
    isLoad = true;
}
);

/**
 * tooltips
 * @description
 * @author 大雄
 * @date 2016年9月17日下午3:22:57
 * @param params
 * @param type
 * @returns {String}
 */
function formatterStr(params,type){
	if(type == 0){
		/*if(params['name'] == '四川 : 甘肃'){
        	return '';
        }
		var str = params['data']['path']+" ";
		var mcorhrTemp = params['data']['mcorhrTemp'];
		for(var key in mcorhrTemp){
			str+= key +": "+mcorhrTemp[key];
		}*/
        return "";
	}else if(type==1){
		if(params['name'].indexOf(">")<0 || params['name'] == '四川 : 甘肃'){
        	return '';
        }
		var str = params['data']['path']+"<br/>";
		var mcorhrTemp = params['data']['mcorhrTemp'];
		for(var key in mcorhrTemp){
			str+= key +": "+mcorhrTemp[key]+"<br/>";
		}
        return str;
	}
}
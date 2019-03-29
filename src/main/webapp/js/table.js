
	function putDatasToTable(tableNode,datas,headAry){
			
			tableNode.innerHTML = "";
			var headdata = tableNode.insertRow(0);
			for (var k = 0; k < headAry.length; k++) {
				var cdata1 = headdata.insertCell(k);
				cdata1.innerHTML = headAry[k];
				cdata1.style.background = "#EBF3FC";
				cdata1.style.fontWeight = "bold";
				cdata1.style.height = "40px";
			}
			
			
			
			for (var i = 0; i < datas.length; i++) {
				var hdata = tableNode.insertRow(i+1);
				var j = 0;
				for ( var op in datas[i]) {
					var cdata = hdata.insertCell(j);
					cdata.innerHTML = datas[i][op];
					j++;
					
				}
			}
			
		}
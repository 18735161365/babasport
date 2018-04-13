<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>babasport-list</title>
<script type="text/javascript">
//修改
function updateSku(id){
// 	<th>销售价格</th>
	$("#p" + id).attr("disabled",false);
// 	<th>库       存</th>
	$("#s" + id).attr("disabled",false);
// 	<th>购买限制</th>
	$("#l" + id).attr("disabled",false);
// 	<th>运       费</th>
	$("#f" + id).attr("disabled",false);
}
//保存
function addSku(id){
// 	<th>销售价格</th>
	var p = $("#p" + id).attr("disabled",true).val();
// 	<th>库       存</th>
	var s = $("#s" + id).attr("disabled",true).val();
// 	<th>购买限制</th>
	var l = $("#l" + id).attr("disabled",true).val();
// 	<th>运       费</th>
	var f = $("#f" + id).attr("disabled",true).val();
	var url = "/sku/addSku.do";
	var params = {"id":id,"deliveFee":f,"price":p,"stock":s,"upperLimit":l};
	$.post(url,params,function(data){
		if(data == "1"){
			alert("保存成功!");
		}else{
			alert("保存失败!");
		}
	},"text");
}
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 库存管理 - 列表</div>
	<div class="clear"></div>
</div>
<div class="body-box">
<form method="post" id="tableForm">
<table cellspacing="1" cellpadding="0" border="0" width="100%" class="pn-ltable">
	<thead class="pn-lthead">
		<tr>
			<th width="20"><input type="checkbox" onclick="Pn.checkbox('ids',this.checked)"/></th>
			<th>商品编号</th>
			<th>商品颜色</th>
			<th>商品尺码</th>
			<th>销售价格</th>
			<th>库       存</th>
			<th>购买限制</th>
			<th>运       费</th>
			<th>是否赠品</th>
			<th>操       作</th>
		</tr>
	</thead>
	<tbody class="pn-ltbody">
	<c:forEach items="${skus }" var="sku">
			<tr bgcolor="#ffffff" onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">
				<td><input type="checkbox" name="ids" value="${sku.id }"/></td>
				<td>${sku.productId }</td>
				<td align="center">${sku.color.name }</td>
				<td align="center">${sku.size }</td>
				<td align="center"><input type="text" id="p${sku.id }" value="${sku.price }" disabled="disabled" size="10"/></td>
				<td align="center"><input type="text" id="s${sku.id }" value="${sku.stock }" disabled="disabled" size="10"/></td>
				<td align="center"><input type="text" id="l${sku.id }" value="${sku.upperLimit }" disabled="disabled" size="10"/></td>
				<td align="center"><input type="text" id="f${sku.id }" value="${sku.deliveFee }" disabled="disabled" size="10"/></td>
				<td align="center">不是</td>
				<td align="center"><a href="javascript:updateSku('${sku.id }')" class="pn-opt">修改</a> | <a href="javascript:addSku('${sku.id }')" class="pn-opt">保存</a></td>
			</tr>
	
	</c:forEach>
		
			
	</tbody>
</table>
</form>
</div>
</body>
</html>
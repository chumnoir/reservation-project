<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.YearMonth"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.HashSet"%>

<%
LocalDate today = LocalDate.now();
YearMonth nowYm = YearMonth.from(today);

// 表示対象の年月（ym パラメータ。なければ今月）
String ymParam = request.getParameter("ym");
YearMonth currentYm;
if (ymParam != null && ymParam.matches("\\d{4}-\\d{2}")) {
	try {
		currentYm = YearMonth.parse(ymParam);
	} catch (Exception e) {
		currentYm = nowYm;
	}
} else {
	currentYm = nowYm;
}

// 過去の月は予約できないため、今月より前には戻れないようにする
if (currentYm.isBefore(nowYm)) {
	currentYm = nowYm;
}

int year = currentYm.getYear();
int month = currentYm.getMonthValue();
int lastDay = currentYm.lengthOfMonth();

LocalDate firstDate = currentYm.atDay(1);
int firstDayOfWeek = firstDate.getDayOfWeek().getValue() % 7;

// 前月・次月（前月は今月より後のときだけ移動可能）
YearMonth prevYm = currentYm.minusMonths(1);
YearMonth nextYm = currentYm.plusMonths(1);
boolean canGoPrev = currentYm.isAfter(nowYm);

String currentYmValue = currentYm.toString(); // 例: 2026-06
String prevYmValue = prevYm.toString();
String nextYmValue = nextYm.toString();

// Servletから受け取る
Set<String> availableDateSet = (Set<String>) request.getAttribute("availableDateSet");
if (availableDateSet == null) {
	availableDateSet = new HashSet<String>();
}

// 選択状態保持
String selectedDate = (String) request.getAttribute("selectedDate");
if (selectedDate == null) {
	selectedDate = request.getParameter("date");
}
if (selectedDate == null) {
	selectedDate = "";
}
%>

<style>
.calendar-wrapper {
	width: 100%;
	max-width: 420px;
	margin: 0 auto;
	font-family: sans-serif;
}

.calendar-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 12px;
}

.calendar-title {
	text-align: center;
	font-size: 18px;
	font-weight: bold;
	flex: 1;
}

.cal-nav-btn {
	width: 36px;
	height: 36px;
	border: 1px solid #c9b79c;
	background-color: #fffdf8;
	color: #6b4423;
	border-radius: 50%;
	font-size: 18px;
	line-height: 1;
	cursor: pointer;
	transition: background-color 0.15s ease;
}

.cal-nav-btn:hover:not(:disabled) {
	background-color: #b96b32;
	color: #ffffff;
	border-color: #a85f2e;
}

.cal-nav-btn:disabled {
	opacity: 0.35;
	cursor: not-allowed;
}

.calendar-table {
	width: 100%;
	border-collapse: collapse;
	table-layout: fixed;
	background-color: #fff;
}

.calendar-table th, .calendar-table td {
	border: 1px solid #999;
	text-align: center;
	vertical-align: middle;
	height: 52px;
	padding: 0;
}

.calendar-table th {
	background-color: #f2f2f2;
}

.sun {
	color: red;
}

.sat {
	color: blue;
}

.empty-cell {
	background-color: #fafafa;
}

.available-day {
	background-color: #ffffff;
}

.available-day label {
	display: flex;
	width: 100%;
	height: 100%;
	align-items: center;
	justify-content: center;
	cursor: pointer;
	font-size: 14px;
}

.available-day input[type="radio"] {
	margin-right: 4px;
}

.unavailable-day {
	background-color: #d9d9d9;
	color: #777;
}

.today-cell {
	outline: 2px solid #666;
	outline-offset: -2px;
}
</style>

<div class="calendar-wrapper">
	<div class="calendar-header">
		<%
		if (canGoPrev) {
		%>
		<button type="button" class="cal-nav-btn"
			onclick="moveMonth(this, '<%=prevYmValue%>')" aria-label="前の月">‹</button>
		<%
		} else {
		%>
		<button type="button" class="cal-nav-btn" disabled aria-label="前の月">‹</button>
		<%
		}
		%>
		<span class="calendar-title"><%=year%>年<%=month%>月</span>
		<button type="button" class="cal-nav-btn"
			onclick="moveMonth(this, '<%=nextYmValue%>')" aria-label="次の月">›</button>
	</div>

	<%-- 表示中の年月をフォームに保持（日付・時間選択で再送信しても月が維持される） --%>
	<input type="hidden" name="ym" value="<%=currentYmValue%>">

	<table class="calendar-table">
		<thead>
			<tr>
				<th class="sun">日</th>
				<th>月</th>
				<th>火</th>
				<th>水</th>
				<th>木</th>
				<th>金</th>
				<th class="sat">土</th>
			</tr>
		</thead>
		<tbody>
			<%
			int day = 1;
			for (int week = 0; week < 6; week++) {
			%>
			<tr>
				<%
				for (int col = 0; col < 7; col++) {
					if (week == 0 && col < firstDayOfWeek) {
				%>
				<td class="empty-cell"></td>
				<%
				} else if (day > lastDay) {
				%>
				<td class="empty-cell"></td>
				<%
				} else {
				// ループ中の日付オブジェクトを生成
				LocalDate loopDate = currentYm.atDay(day);
				String dateValue = String.format("%04d-%02d-%02d", year, month, day);

				// 💡 DBに存在し、かつ「今日以降」である場合のみ予約可能とする
				boolean available = availableDateSet.contains(dateValue) && !loopDate.isBefore(today);

				boolean isToday = today.getYear() == year
						&& today.getMonthValue() == month
						&& today.getDayOfMonth() == day;

				String tdClass = available ? "available-day" : "unavailable-day";
				if (isToday) {
					tdClass += " today-cell";
				}

				if (available) {
				%>
				<td class="<%=tdClass%>"><label> <input type="radio"
						name="date" value="<%=dateValue%>"
						<%=dateValue.equals(selectedDate) ? "checked" : ""%>
						onchange="this.form.submit()"> <%=day%>
				</label></td>
				<%
				} else {
				%>
				<td class="<%=tdClass%>"><%=day%></td>
				<%
				}
				day++;
				}
				}
				%>
			</tr>
			<%
			if (day > lastDay) {
				break;
			}
			}
			%>
		</tbody>
	</table>
</div>

<script>
// 前月／次月ボタン: 隠しフィールド ym を書き換えてフォームを再送信する
function moveMonth(btn, ym) {
	var form = btn.form;
	if (!form) {
		return;
	}
	var field = form.querySelector('input[name="ym"]');
	if (field) {
		field.value = ym;
	}
	form.submit();
}
</script>

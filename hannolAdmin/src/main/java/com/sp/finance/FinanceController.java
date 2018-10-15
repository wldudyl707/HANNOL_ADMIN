
package com.sp.finance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;

@Controller("finance.chartController")
public class FinanceController {
	@Autowired
	FinanceServiceImpl service;

	@Autowired
	private MyExcelView excelView;

	@RequestMapping(value = "/finance/main")
	public String main(@RequestParam(defaultValue = "0") int year, @RequestParam(defaultValue = "quarter") String gubun,
			Model model) throws Exception {
		if (year == 0) {
			year = Calendar.getInstance().get(Calendar.YEAR);
		}

		Map<String, Object> map = new HashMap<>();
		map.put("year", year);
		List<Finance> list = service.getYears();

		model.addAttribute("yearList", list);

		return ".finance.main";
	}

	@RequestMapping(value = "/finance/profit")
	public String profit(@RequestParam(defaultValue = "0") int year,
			@RequestParam(defaultValue = "quarter") String gubun, Model model) throws Exception {
		if (year == 0) {
			year = Calendar.getInstance().get(Calendar.YEAR);
		}

		Map<String, Object> map = new HashMap<>();
		map.put("year", year);
		List<Finance> list = service.getYearsPayment();

		model.addAttribute("yearList", list);

		return ".finance.profit";
	}

	@RequestMapping(value = "/finance/loss")
	public String loss(@RequestParam(defaultValue = "0") int year, @RequestParam(defaultValue = "quarter") String gubun,
			Model model) throws Exception {

		if (year == 0) {
			year = Calendar.getInstance().get(Calendar.YEAR);
		}

		Map<String, Object> map = new HashMap<>();
		map.put("year", year);
		List<Finance> list = service.getYearsLoss();

		model.addAttribute("yearList", list);

		return ".finance.loss";
	}

	@RequestMapping(value = "/finance/financeChart", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String financeChart(@RequestParam(defaultValue = "0") int year,
			@RequestParam(defaultValue = "quarter") String gubun) throws Exception {
		JSONArray arr = new JSONArray();
		JSONObject ob;
		JSONObject job;

		if (year == 0) {
			year = Calendar.getInstance().get(Calendar.YEAR);
		}

		// 매출sql부르자!!!
		Map<String, Object> map = new HashMap<>();
		map.put("year", year);

		List<Finance> listP = service.getSalePayment(map);

		if (gubun.equals("quarter")) {
			// 분기별

			double[] paySales = new double[4];
			String[] chartX = new String[4];

			for (int i = 0; i < 4; i++) {
				chartX[i] = year + "년 " + (i + 1) + "분기";
			}

			for (Finance f : listP) {
				if (f.getQuarter() == 1) {
					paySales[0] = f.getSaleAmount();
				} else if (f.getQuarter() == 2) {
					paySales[1] = f.getSaleAmount();
				} else if (f.getQuarter() == 3) {
					paySales[2] = f.getSaleAmount();
				} else if (f.getQuarter() == 4) {
					paySales[3] = f.getSaleAmount();
				}
			}

			ob = new JSONObject();
			ob.put("name", "분기별 매출");
			ob.put("data", paySales);
			arr.put(ob);

			// 지출sql부르자!!! : 수리 + 입고
			List<Finance> listR = service.getExpenseRepair(map);
			List<Finance> listG = service.getExpenseGoodsIn(map);

			double[] expense = new double[4];

			for (Finance f : listR) {
				if (f.getQuarter() == 1) {
					expense[0] = f.getSaleAmount();
				} else if (f.getQuarter() == 2) {
					expense[1] = f.getSaleAmount();
				} else if (f.getQuarter() == 3) {
					expense[2] = f.getSaleAmount();
				} else if (f.getQuarter() == 4) {
					expense[3] = f.getSaleAmount();
				}
			}

			for (Finance f : listG) {
				if (f.getQuarter() == 1) {
					expense[0] += f.getSaleAmount();
				} else if (f.getQuarter() == 2) {
					expense[1] += f.getSaleAmount();
				} else if (f.getQuarter() == 3) {
					expense[2] += f.getSaleAmount();
				} else if (f.getQuarter() == 4) {
					expense[3] += f.getSaleAmount();
				}
			}

			ob = new JSONObject();
			ob.put("name", "분기별 지출");
			ob.put("data", expense);
			arr.put(ob);

			job = new JSONObject();
			job.put("year", year);
			job.put("series", arr);
			job.put("chartX", chartX);
		} else {
			// 월별
			double[] paySales = new double[12];
			String[] chartX = new String[12];

			for (int i = 0; i < 12; i++) {
				chartX[i] = (i + 1) + "월";
			}

			for (Finance f : listP) {
				if (f.getMonth() == 1) {
					paySales[0] = f.getSaleAmount();
				} else if (f.getMonth() == 2) {
					paySales[1] = f.getSaleAmount();
				} else if (f.getMonth() == 3) {
					paySales[2] = f.getSaleAmount();
				} else if (f.getMonth() == 4) {
					paySales[3] = f.getSaleAmount();
				} else if (f.getMonth() == 5) {
					paySales[4] = f.getSaleAmount();
				} else if (f.getMonth() == 6) {
					paySales[5] = f.getSaleAmount();
				} else if (f.getMonth() == 7) {
					paySales[6] = f.getSaleAmount();
				} else if (f.getMonth() == 8) {
					paySales[7] = f.getSaleAmount();
				} else if (f.getMonth() == 9) {
					paySales[8] = f.getSaleAmount();
				} else if (f.getMonth() == 10) {
					paySales[9] = f.getSaleAmount();
				} else if (f.getMonth() == 11) {
					paySales[10] = f.getSaleAmount();
				} else if (f.getMonth() == 12) {
					paySales[11] = f.getSaleAmount();
				}
			}

			ob = new JSONObject();
			ob.put("name", "월별 매출");
			ob.put("data", paySales);
			arr.put(ob);

			// 지출sql부르자!!! : 수리 + 입고
			List<Finance> listR = service.getExpenseRepair(map);
			List<Finance> listG = service.getExpenseGoodsIn(map);

			double[] expense = new double[12];

			for (Finance f : listR) {
				if (f.getMonth() == 1) {
					expense[0] = f.getSaleAmount();
				} else if (f.getMonth() == 2) {
					expense[1] = f.getSaleAmount();
				} else if (f.getMonth() == 3) {
					expense[2] = f.getSaleAmount();
				} else if (f.getMonth() == 4) {
					expense[3] = f.getSaleAmount();
				} else if (f.getMonth() == 5) {
					expense[4] = f.getSaleAmount();
				} else if (f.getMonth() == 6) {
					expense[5] = f.getSaleAmount();
				} else if (f.getMonth() == 7) {
					expense[6] = f.getSaleAmount();
				} else if (f.getMonth() == 8) {
					expense[7] = f.getSaleAmount();
				} else if (f.getMonth() == 9) {
					expense[8] = f.getSaleAmount();
				} else if (f.getMonth() == 10) {
					expense[9] = f.getSaleAmount();
				} else if (f.getMonth() == 11) {
					expense[10] = f.getSaleAmount();
				} else if (f.getMonth() == 12) {
					expense[11] = f.getSaleAmount();
				}
			}

			for (Finance f : listG) {
				if (f.getMonth() == 1) {
					expense[0] += f.getSaleAmount();
				} else if (f.getMonth() == 2) {
					expense[1] += f.getSaleAmount();
				} else if (f.getMonth() == 3) {
					expense[2] += f.getSaleAmount();
				} else if (f.getMonth() == 4) {
					expense[3] += f.getSaleAmount();
				} else if (f.getMonth() == 5) {
					expense[4] += f.getSaleAmount();
				} else if (f.getMonth() == 6) {
					expense[5] += f.getSaleAmount();
				} else if (f.getMonth() == 7) {
					expense[6] += f.getSaleAmount();
				} else if (f.getMonth() == 8) {
					expense[7] += f.getSaleAmount();
				} else if (f.getMonth() == 9) {
					expense[8] += f.getSaleAmount();
				} else if (f.getMonth() == 10) {
					expense[9] += f.getSaleAmount();
				} else if (f.getMonth() == 11) {
					expense[10] += f.getSaleAmount();
				} else if (f.getMonth() == 12) {
					expense[11] += f.getSaleAmount();
				}
			}

			ob = new JSONObject();
			ob.put("name", "월별 지출");
			ob.put("data", expense);
			arr.put(ob);

			job = new JSONObject();
			job.put("year", year);
			job.put("series", arr);
			job.put("chartX", chartX);
		}

		return job.toString();

	}

	@RequestMapping(value = "/finance/financeChartPeriod", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String financeChartPeriod(@RequestParam String startDate, @RequestParam String endDate) throws Exception {
		JSONArray arr = new JSONArray();
		JSONObject ob = new JSONObject();
		JSONObject job = new JSONObject();

		Map<String, Object> map = new HashMap<>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		List<Finance> list = service.profitLinePeriod(map);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date start = formatter.parse(startDate);
		Date end = formatter.parse(endDate);

		long diff = end.getTime() - start.getTime();
		int diffDays = (int) (diff / (24 * 60 * 60 * 1000));

		double[] paySales = new double[diffDays];
		String[] chartX = new String[diffDays];

		Calendar cal = new GregorianCalendar(Locale.KOREA);

		int tempnum = 0;

		for (int i = 0; i < diffDays; i++) {
			cal.setTime(start);
			cal.add(Calendar.DAY_OF_YEAR, i);

			String temp = formatter.format(cal.getTime());
			chartX[i] = temp;

			if (tempnum < list.size()) {
				if (temp.equals(list.get(tempnum).getPayDate())) {
					paySales[i] += list.get(tempnum).getSaleAmount();
					tempnum++;
				}
			}

		}

		ob = new JSONObject();
		ob.put("name", "조회기간 매출");
		ob.put("data", paySales);
		arr.put(ob);

		List<Finance> listR = service.expenseRepairPeriod(map);
		List<Finance> listG = service.expenseGoodsInPeriod(map);

		double[] expenses = new double[diffDays];

		tempnum = 0;

		for (int i = 0; i < diffDays; i++) {
			cal.setTime(start);
			cal.add(Calendar.DAY_OF_YEAR, i);

			String temp = formatter.format(cal.getTime());
			chartX[i] = temp;

			if (tempnum < listR.size()) {

				if (temp.equals(listR.get(tempnum).getRepairDate())) {

					expenses[i] += listR.get(tempnum).getSaleAmount();
					tempnum++;
				}
			}

		}

		cal = new GregorianCalendar(Locale.KOREA);
		tempnum = 0;

		for (int i = 0; i < diffDays; i++) {
			cal.setTime(start);
			cal.add(Calendar.DAY_OF_YEAR, i);

			String temp = formatter.format(cal.getTime());
			chartX[i] = temp;

			if (tempnum < listG.size()) {

				if (temp.equals(listG.get(tempnum).getInDate())) {

					expenses[i] += listG.get(tempnum).getSaleAmount();
					tempnum++;
				}
			}

		}

		ob = new JSONObject();
		ob.put("name", "조회기간 지출");
		ob.put("data", expenses);
		arr.put(ob);

		job = new JSONObject();
		job.put("series", arr);
		job.put("chartX", chartX);

		return job.toString();
	}

	@RequestMapping(value = "/finance/profitLine", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String profitLine(@RequestParam(defaultValue = "0") int year,
			@RequestParam(defaultValue = "quarter") String gubun) throws Exception {
		JSONArray arr = new JSONArray();
		JSONObject ob = new JSONObject();
		JSONObject job = new JSONObject();

		if (year == 0) {
			year = Calendar.getInstance().get(Calendar.YEAR);
		}

		Map<String, Object> map = new HashMap<>();
		map.put("year", year);

		List<Finance> listP = service.getSalePayment(map);
		List<Finance> listT = service.getSaleTicket(map);
		List<Finance> listG = service.getSaleGift(map);

		if (gubun.equals("quarter")) {

			double[] paySales = new double[4];
			double[] payTicket = new double[4];
			double[] payGift = new double[4];
			String[] chartX = new String[4];

			for (int i = 0; i < 4; i++) {
				chartX[i] = year + "년 " + (i + 1) + "분기";
			}

			for (Finance f : listP) {
				if (f.getQuarter() == 1) {
					paySales[0] += f.getSaleAmount();
				} else if (f.getQuarter() == 2) {
					paySales[1] += f.getSaleAmount();
				} else if (f.getQuarter() == 3) {
					paySales[2] += f.getSaleAmount();
				} else if (f.getQuarter() == 4) {
					paySales[3] += f.getSaleAmount();
				}
			}
			
			for (Finance f : listT) {
				if (f.getQuarter() == 1) {
					payTicket[0] += f.getSaleAmount();
				} else if (f.getQuarter() == 2) {
					payTicket[1] += f.getSaleAmount();
				} else if (f.getQuarter() == 3) {
					payTicket[2] += f.getSaleAmount();
				} else if (f.getQuarter() == 4) {
					payTicket[3] += f.getSaleAmount();
				}
			}
			
			for (Finance f : listG) {
				if (f.getQuarter() == 1) {
					payGift[0] += f.getSaleAmount();
				} else if (f.getQuarter() == 2) {
					payGift[1] += f.getSaleAmount();
				} else if (f.getQuarter() == 3) {
					payGift[2] += f.getSaleAmount();
				} else if (f.getQuarter() == 4) {
					payGift[3] += f.getSaleAmount();
				}
			}

			ob.put("name", "분기별 매출");
			ob.put("data", paySales);
			arr.put(ob);
			
			ob = new JSONObject();
			ob.put("name", "분기별 이용권 매출");
			ob.put("data", payTicket);
			arr.put(ob);
			
			ob = new JSONObject();
			ob.put("name", "분기별 기프트샵 매출");
			ob.put("data", payGift);
			arr.put(ob);

			job = new JSONObject();
			job.put("year", year);
			job.put("series", arr);
			job.put("chartX", chartX);

		} else {
			double[] paySales = new double[12];
			double[] payTicket = new double[12];
			double[] payGift = new double[12];
			String[] chartX = new String[12];

			for (int i = 0; i < 12; i++) {
				chartX[i] = (i + 1) + "월";
			}

			for (Finance f : listP) {
				if (f.getMonth() == 1) {
					paySales[0] += f.getSaleAmount();
				} else if (f.getMonth() == 2) {
					paySales[1] += f.getSaleAmount();
				} else if (f.getMonth() == 3) {
					paySales[2] += f.getSaleAmount();
				} else if (f.getMonth() == 4) {
					paySales[3] += f.getSaleAmount();
				} else if (f.getMonth() == 5) {
					paySales[4] += f.getSaleAmount();
				} else if (f.getMonth() == 6) {
					paySales[5] += f.getSaleAmount();
				} else if (f.getMonth() == 7) {
					paySales[6] += f.getSaleAmount();
				} else if (f.getMonth() == 8) {
					paySales[7] += f.getSaleAmount();
				} else if (f.getMonth() == 9) {
					paySales[8] += f.getSaleAmount();
				} else if (f.getMonth() == 10) {
					paySales[9] += f.getSaleAmount();
				} else if (f.getMonth() == 11) {
					paySales[10] += f.getSaleAmount();
				} else if (f.getMonth() == 12) {
					paySales[11] += f.getSaleAmount();
				}
			}
			
			for (Finance f : listT) {
				if (f.getMonth() == 1) {
					payTicket[0] += f.getSaleAmount();
				} else if (f.getMonth() == 2) {
					payTicket[1] += f.getSaleAmount();
				} else if (f.getMonth() == 3) {
					payTicket[2] += f.getSaleAmount();
				} else if (f.getMonth() == 4) {
					payTicket[3] += f.getSaleAmount();
				} else if (f.getMonth() == 5) {
					payTicket[4] += f.getSaleAmount();
				} else if (f.getMonth() == 6) {
					payTicket[5] += f.getSaleAmount();
				} else if (f.getMonth() == 7) {
					payTicket[6] += f.getSaleAmount();
				} else if (f.getMonth() == 8) {
					payTicket[7] += f.getSaleAmount();
				} else if (f.getMonth() == 9) {
					payTicket[8] += f.getSaleAmount();
				} else if (f.getMonth() == 10) {
					payTicket[9] += f.getSaleAmount();
				} else if (f.getMonth() == 11) {
					payTicket[10] += f.getSaleAmount();
				} else if (f.getMonth() == 12) {
					payTicket[11] += f.getSaleAmount();
				}
			}
			
			for (Finance f : listG) {
				if (f.getMonth() == 1) {
					payGift[0] += f.getSaleAmount();
				} else if (f.getMonth() == 2) {
					payGift[1] += f.getSaleAmount();
				} else if (f.getMonth() == 3) {
					payGift[2] += f.getSaleAmount();
				} else if (f.getMonth() == 4) {
					payGift[3] += f.getSaleAmount();
				} else if (f.getMonth() == 5) {
					payGift[4] += f.getSaleAmount();
				} else if (f.getMonth() == 6) {
					payGift[5] += f.getSaleAmount();
				} else if (f.getMonth() == 7) {
					payGift[6] += f.getSaleAmount();
				} else if (f.getMonth() == 8) {
					payGift[7] += f.getSaleAmount();
				} else if (f.getMonth() == 9) {
					payGift[8] += f.getSaleAmount();
				} else if (f.getMonth() == 10) {
					payGift[9] += f.getSaleAmount();
				} else if (f.getMonth() == 11) {
					payGift[10] += f.getSaleAmount();
				} else if (f.getMonth() == 12) {
					payGift[11] += f.getSaleAmount();
				}
			}

			ob.put("name", "월별 매출");
			ob.put("data", paySales);
			arr.put(ob);
			
			ob = new JSONObject();
			ob.put("name", "월별 이용권 매출");
			ob.put("data", payTicket);
			arr.put(ob);
			
			ob = new JSONObject();
			ob.put("name", "월별 기프트샵 매출");
			ob.put("data", payGift);
			arr.put(ob);

			job = new JSONObject();
			job.put("year", year);
			job.put("series", arr);
			job.put("chartX", chartX);
		}
		
		return job.toString();
	}

	@RequestMapping(value = "/finance/profitLinePeriod", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String profitLinePeriod(@RequestParam String startDate, @RequestParam String endDate) throws Exception {
		JSONArray arr = new JSONArray();
		JSONObject ob = new JSONObject();
		JSONObject job = new JSONObject();

		Map<String, Object> map = new HashMap<>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		List<Finance> list = service.profitLinePeriod(map);
		List<Finance> listT = service.ticketLinePeriod(map);
		List<Finance> listG = service.giftLinePeriod(map);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date start = formatter.parse(startDate);
		Date end = formatter.parse(endDate);

		long diff = end.getTime() - start.getTime();
		int diffDays = (int) (diff / (24 * 60 * 60 * 1000));

		double[] paySales = new double[diffDays];
		double[] ticketSales = new double[diffDays];
		double[] giftSales = new double[diffDays];
		String[] chartX = new String[diffDays];

		Calendar cal = new GregorianCalendar(Locale.KOREA);

		int tempnum = 0;

		for (int i = 0; i < diffDays; i++) {
			cal.setTime(start);
			cal.add(Calendar.DAY_OF_YEAR, i);

			String temp = formatter.format(cal.getTime());
			chartX[i] = temp;

			if (tempnum < list.size()) {
				if (temp.equals(list.get(tempnum).getPayDate())) {
					paySales[i] += list.get(tempnum).getSaleAmount();
					tempnum++;
				}
			}
		}
		
		tempnum = 0;

		for (int i = 0; i < diffDays; i++) {
			cal.setTime(start);
			cal.add(Calendar.DAY_OF_YEAR, i);

			String temp = formatter.format(cal.getTime());
			chartX[i] = temp;

			if (tempnum < listT.size()) {
				if (temp.equals(listT.get(tempnum).getPayDate())) {
					ticketSales[i] += listT.get(tempnum).getSaleAmount();
					tempnum++;
				}
			}
		}
		
		tempnum = 0;

		for (int i = 0; i < diffDays; i++) {
			cal.setTime(start);
			cal.add(Calendar.DAY_OF_YEAR, i);

			String temp = formatter.format(cal.getTime());
			chartX[i] = temp;

			if (tempnum < listG.size()) {
				if (temp.equals(listG.get(tempnum).getPayDate())) {
					giftSales[i] += listG.get(tempnum).getSaleAmount();
					tempnum++;
				}
			}
		}

		ob.put("name", "조회기간 매출");
		ob.put("data", paySales);
		arr.put(ob);
		
		ob = new JSONObject();
		ob.put("name", "조회기간 이용권 매출");
		ob.put("data", ticketSales);
		arr.put(ob);
		
		ob = new JSONObject();
		ob.put("name", "조회기간 기프트샵 매출");
		ob.put("data", giftSales);
		arr.put(ob);

		job = new JSONObject();
		job.put("series", arr);
		job.put("chartX", chartX);

		return job.toString();
	}

	@RequestMapping(value = "/finance/lossLine", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String lossLine(@RequestParam(defaultValue = "0") int year,
			@RequestParam(defaultValue = "quarter") String gubun) throws Exception {
		JSONArray arr = new JSONArray();
		JSONObject ob = new JSONObject();
		JSONObject job = new JSONObject();

		if (year == 0) {
			year = Calendar.getInstance().get(Calendar.YEAR);
		}

		Map<String, Object> map = new HashMap<>();
		map.put("year", year);

		List<Finance> listR = service.getExpenseRepair(map);
		List<Finance> listG = service.getExpenseGoodsIn(map);

		if (gubun.equals("quarter")) {

			double[] expenses = new double[4];
			double[] expensesR = new double[4];
			double[] expensesG = new double[4];
			String[] chartX = new String[4];

			for (int i = 0; i < 4; i++) {
				chartX[i] = year + "년 " + (i + 1) + "분기";
			}

			for (Finance f : listR) {
				if (f.getQuarter() == 1) {
					expenses[0] += f.getSaleAmount();
					expensesR[0] += f.getSaleAmount();
				} else if (f.getQuarter() == 2) {
					expenses[1] += f.getSaleAmount();
					expensesR[1] += f.getSaleAmount();
				} else if (f.getQuarter() == 3) {
					expenses[2] += f.getSaleAmount();
					expensesR[2] += f.getSaleAmount();
				} else if (f.getQuarter() == 4) {
					expenses[3] += f.getSaleAmount();
					expensesR[3] += f.getSaleAmount();
				}
			}

			for (Finance f : listG) {
				if (f.getQuarter() == 1) {
					expenses[0] += f.getSaleAmount();
					expensesG[0] += f.getSaleAmount();
				} else if (f.getQuarter() == 2) {
					expenses[1] += f.getSaleAmount();
					expensesG[1] += f.getSaleAmount();
				} else if (f.getQuarter() == 3) {
					expenses[2] += f.getSaleAmount();
					expensesG[2] += f.getSaleAmount();
				} else if (f.getQuarter() == 4) {
					expenses[3] += f.getSaleAmount();
					expensesG[3] += f.getSaleAmount();
				}
			}

			ob.put("name", "분기별 지출");
			ob.put("data", expenses);
			arr.put(ob);

			ob = new JSONObject();
			ob.put("name", "수리 분기별 지출");
			ob.put("data", expensesR);
			arr.put(ob);

			ob = new JSONObject();
			ob.put("name", "구매 분기별 지출");
			ob.put("data", expensesG);
			arr.put(ob);

			job = new JSONObject();
			job.put("year", year);
			job.put("series", arr);
			job.put("chartX", chartX);

		} else {
			double[] expenses = new double[12];
			double[] expensesR = new double[12];
			double[] expensesG = new double[12];
			String[] chartX = new String[12];

			for (int i = 0; i < 12; i++) {
				chartX[i] = (i + 1) + "월";
			}

			for (Finance f : listR) {
				if (f.getMonth() == 1) {
					expenses[0] += f.getSaleAmount();
					expensesR[0] += f.getSaleAmount();
				} else if (f.getMonth() == 2) {
					expenses[1] += f.getSaleAmount();
					expensesR[1] += f.getSaleAmount();
				} else if (f.getMonth() == 3) {
					expenses[2] += f.getSaleAmount();
					expensesR[2] += f.getSaleAmount();
				} else if (f.getMonth() == 4) {
					expenses[3] += f.getSaleAmount();
					expensesR[3] += f.getSaleAmount();
				} else if (f.getMonth() == 5) {
					expenses[4] += f.getSaleAmount();
					expensesR[4] += f.getSaleAmount();
				} else if (f.getMonth() == 6) {
					expenses[5] += f.getSaleAmount();
					expensesR[5] += f.getSaleAmount();
				} else if (f.getMonth() == 7) {
					expenses[6] += f.getSaleAmount();
					expensesR[6] += f.getSaleAmount();
				} else if (f.getMonth() == 8) {
					expenses[7] += f.getSaleAmount();
					expensesR[7] += f.getSaleAmount();
				} else if (f.getMonth() == 9) {
					expenses[8] += f.getSaleAmount();
					expensesR[8] += f.getSaleAmount();
				} else if (f.getMonth() == 10) {
					expenses[9] += f.getSaleAmount();
					expensesR[9] += f.getSaleAmount();
				} else if (f.getMonth() == 11) {
					expenses[10] += f.getSaleAmount();
					expensesR[10] += f.getSaleAmount();
				} else if (f.getMonth() == 12) {
					expenses[11] += f.getSaleAmount();
					expensesR[11] += f.getSaleAmount();
				}
			}

			for (Finance f : listG) {
				if (f.getMonth() == 1) {
					expenses[0] += f.getSaleAmount();
					expensesG[0] += f.getSaleAmount();
				} else if (f.getMonth() == 2) {
					expenses[1] += f.getSaleAmount();
					expensesG[1] += f.getSaleAmount();
				} else if (f.getMonth() == 3) {
					expenses[2] += f.getSaleAmount();
					expensesG[2] += f.getSaleAmount();
				} else if (f.getMonth() == 4) {
					expenses[3] += f.getSaleAmount();
					expensesG[3] += f.getSaleAmount();
				} else if (f.getMonth() == 5) {
					expenses[4] += f.getSaleAmount();
					expensesG[4] += f.getSaleAmount();
				} else if (f.getMonth() == 6) {
					expenses[5] += f.getSaleAmount();
					expensesG[5] += f.getSaleAmount();
				} else if (f.getMonth() == 7) {
					expenses[6] += f.getSaleAmount();
					expensesG[6] += f.getSaleAmount();
				} else if (f.getMonth() == 8) {
					expenses[7] += f.getSaleAmount();
					expensesG[7] += f.getSaleAmount();
				} else if (f.getMonth() == 9) {
					expenses[8] += f.getSaleAmount();
					expensesG[8] += f.getSaleAmount();
				} else if (f.getMonth() == 10) {
					expenses[9] += f.getSaleAmount();
					expensesG[9] += f.getSaleAmount();
				} else if (f.getMonth() == 11) {
					expenses[10] += f.getSaleAmount();
					expensesG[10] += f.getSaleAmount();
				} else if (f.getMonth() == 12) {
					expenses[11] += f.getSaleAmount();
					expensesG[11] += f.getSaleAmount();
				}
			}

			ob.put("name", "월별 지출");
			ob.put("data", expenses);
			arr.put(ob);

			ob = new JSONObject();
			ob.put("name", "수리 분기별 지출");
			ob.put("data", expensesR);
			arr.put(ob);

			ob = new JSONObject();
			ob.put("name", "구매 분기별 지출");
			ob.put("data", expensesG);
			arr.put(ob);

			job = new JSONObject();
			job.put("year", year);
			job.put("series", arr);
			job.put("chartX", chartX);
		}

		return job.toString();
	}

	@RequestMapping(value = "/finance/lossLinePeriod", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String lossLinePeriod(@RequestParam String startDate, @RequestParam String endDate) throws Exception {
		JSONArray arr = new JSONArray();
		JSONObject ob = new JSONObject();
		JSONObject job = new JSONObject();

		Map<String, Object> map = new HashMap<>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		List<Finance> listR = service.expenseRepairPeriod(map);
		List<Finance> listG = service.expenseGoodsInPeriod(map);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date start = formatter.parse(startDate);
		Date end = formatter.parse(endDate);

		long diff = end.getTime() - start.getTime();
		int diffDays = (int) (diff / (24 * 60 * 60 * 1000));

		double[] expenses = new double[diffDays];
		double[] expensesR = new double[diffDays];
		double[] expensesG = new double[diffDays];
		String[] chartX = new String[diffDays];

		Calendar cal = new GregorianCalendar(Locale.KOREA);

		int tempnum = 0;

		// test
		for (Finance t : listR) {
			System.out.println(t.getRepairDate());
		}

		for (int i = 0; i < diffDays; i++) {
			cal.setTime(start);
			cal.add(Calendar.DAY_OF_YEAR, i);

			String temp = formatter.format(cal.getTime());
			chartX[i] = temp;

			if (tempnum < listR.size()) {

				if (temp.equals(listR.get(tempnum).getRepairDate())) {

					expenses[i] += listR.get(tempnum).getSaleAmount();
					expensesR[i] += listR.get(tempnum).getSaleAmount();
					tempnum++;
				}
			}

		}

		cal = new GregorianCalendar(Locale.KOREA);
		tempnum = 0;

		for (int i = 0; i < diffDays; i++) {
			cal.setTime(start);
			cal.add(Calendar.DAY_OF_YEAR, i);

			String temp = formatter.format(cal.getTime());
			chartX[i] = temp;

			if (tempnum < listG.size()) {

				if (temp.equals(listG.get(tempnum).getInDate())) {

					expenses[i] += listG.get(tempnum).getSaleAmount();
					expensesG[i] += listG.get(tempnum).getSaleAmount();
					tempnum++;
				}
			}

		}

		ob.put("name", "조회기간 지출");
		ob.put("data", expenses);
		arr.put(ob);

		ob = new JSONObject();
		ob.put("name", "수리 조회기간 지출");
		ob.put("data", expensesR);
		arr.put(ob);

		ob = new JSONObject();
		ob.put("name", "구매 조회기간 지출");
		ob.put("data", expensesG);
		arr.put(ob);

		job = new JSONObject();
		job.put("series", arr);
		job.put("chartX", chartX);

		return job.toString();
	}

	@RequestMapping(value = "/finance/excel")
	public View excel(@RequestParam(value = "financeYear", defaultValue = "0") int financeYear,
			Map<String, Object> model) throws Exception {

		if (financeYear == 0) {
			financeYear = Calendar.getInstance().get(Calendar.YEAR);
		}

		String sheetName1 = "매출";
		String sheetName2 = "지출(구매)";
		String sheetName3 = "지출(수리)";
		String sheetName4 = "할인";
		String filename = "finance.xls";

		List<String> columnLabels = new ArrayList<>();
		List<Object[]> columnValues1 = new ArrayList<>();
		List<Object[]> columnValues2 = new ArrayList<>();
		List<Object[]> columnValues3 = new ArrayList<>();
		List<Object[]> columnValues4 = new ArrayList<>();

		Map<String, Object> map = new HashMap<>();
		map.put("year", financeYear);

		List<Finance> list = service.getProfitData(map);

		columnLabels.add("년도");
		columnLabels.add("분기");
		columnLabels.add("월");
		columnLabels.add("일");
		columnLabels.add("비고");
		columnLabels.add("금액");

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getParentCode() == 1) {
				columnValues1.add(new Object[] { financeYear, list.get(i).getQuarter(), list.get(i).getMonth(),
						list.get(i).getDay(), "이용권", list.get(i).getSaleAmount() });
			} else {
				columnValues1.add(new Object[] { financeYear, list.get(i).getQuarter(), list.get(i).getMonth(),
						list.get(i).getDay(), "기프트샵", list.get(i).getSaleAmount() });
			}
		}

		List<Finance> list2 = service.getLossGoodsData(map);
		for (int i = 0; i < list2.size(); i++) {
			columnValues2.add(new Object[] { financeYear, list2.get(i).getQuarter(), list2.get(i).getMonth(),
					list2.get(i).getDay(), list2.get(i).getMemo(), list2.get(i).getSaleAmount() });
		}
		
		List<Finance> list3 = service.getLossRepairData(map);
		for (int i = 0; i < list3.size(); i++) {
			columnValues3.add(new Object[] { financeYear, list3.get(i).getQuarter(), list3.get(i).getMonth(),
					list3.get(i).getDay(), list3.get(i).getMemo(), list3.get(i).getSaleAmount() });
		}
		
		List<Finance> list4 = service.getLossDiscountData(map);
		for (int i = 0; i < list4.size(); i++) {
			columnValues4.add(new Object[] { financeYear, list4.get(i).getQuarter(), list4.get(i).getMonth(),
					list4.get(i).getDay(), list4.get(i).getMemo(), list4.get(i).getSaleAmount() });
		}

		model.put("sheetName1", sheetName1);
		model.put("sheetName2", sheetName2);
		model.put("sheetName3", sheetName3);
		model.put("sheetName4", sheetName4);
		model.put("filename", filename);
		model.put("columnLabels", columnLabels);
		model.put("columnValues1", columnValues1);
		model.put("columnValues2", columnValues2);
		model.put("columnValues3", columnValues3);
		model.put("columnValues4", columnValues4);

		return excelView;
	}

}

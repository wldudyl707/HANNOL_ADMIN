package com.sp.noticeS;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.common.dao.CommonDAO;
import com.sp.notice.Notice;
import com.sp.staff.Staff;

@Service("noticeS.noticeService")
public class NoticeServiceImpl implements NoticeService {
	@Autowired
	private CommonDAO dao;

	@Override
	public Staff readStaff() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertNotice(Notice dto) {
		int result = 0;
		try {
			dao.insertData("noticeS.insertNotice", dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int dataCount(Map<String, Object> map) {
		int result = 0;
		try {
			result = dao.selectOne("noticeS.dataCount", map);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

	@Override
	public List<Notice> listNotice(Map<String, Object> map) {
		List<Notice> list = null;
		try {
			list = dao.selectList("noticeS.listNotice", map);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return list;
	}

	@Override
	public List<Notice> listOnlyNotice(Map<String, Object> map) {
		List<Notice> list =null;
		try {
			list = dao.selectList("noticeS.listOnlyNotice", map);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return list;
	}

	@Override
	public Notice readNotice(int noticeCode) {
		Notice dto =null;
		try {
			dto =dao.selectOne("noticeS.readNotice", noticeCode);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return dto;
	}

	@Override
	public Notice preReadNotice(Map<String, Object> map) {
		Notice dto = null;
		try {
			dto =dao.selectOne("noticeS.preReadNotice", map);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return dto;
	}

	@Override
	public Notice nextReadNotice(Map<String, Object> map) {
		Notice dto = null;
		try {
			dto =dao.selectOne("noticeS.nextReadNotice", map);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return dto;
	}

	@Override
	public int updateNotice(Notice dto) {
		int result = 0;
		try {
			dao.updateData("noticeS.updateNotice", dto);
			result =1;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

	@Override
	public int deleteNotice(int num, long usersCode) {
		int result = 0;
		try {
			Notice dto = dao.selectOne("noticeS.deleteNotice", num);
			boolean amIAdmin = dao.selectOne("staff.amIAdmin", usersCode);
			
			if(dto==null || ( !amIAdmin &&  usersCode != dto.getUsersCode()))
				return result;
			
			result = dao.deleteData("noticeS.deleteNotice", num);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

}

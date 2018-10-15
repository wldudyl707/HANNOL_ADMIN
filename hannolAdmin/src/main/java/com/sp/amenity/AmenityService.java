package com.sp.amenity;

import java.util.List;
import java.util.Map;

public interface AmenityService {
	public List<Amenity> checkList(Map<String,Object> map);
	public int datanum(Map<String,Object> map);
	public int cancel(Map<String, Object> map);
	public int lockercancel(Map<String, Object> map);
	public List<Amenity> checknum(Map<String, Object> map);
	public int checkcc(Map<String, Object> map);
	
	public Amenity getCubeBookCancelDay(int payCode);
	public int okTicketIfPayCancledCube(int payCode);
	public int payBookCode(int payCode);
	public int paydeleteLocker(int cubepayCode);
	public int paydeleteBook(int cubepayCode);
}

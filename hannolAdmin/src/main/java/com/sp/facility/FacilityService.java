package com.sp.facility;

import java.util.List;
import java.util.Map;

public interface FacilityService {
	public int insertFacility(Facility dto, String pathname);
	public int insertRides(int code);
	public int insertRidesInfo(Facility dto);
	public int updateFacility(Facility dto, String pathname);
	public int deleteFacility(int num, String pathname);
	public List<Facility> listFacility(Map<String,Object> map, String pathname);
	public int dataCount(Map<String,Object> map);
	public Facility readFacility(int num);
	public Facility readFacility(Facility dto);
	public int readFacilityCode();
	public List<Facility> selectCreate();
}

package cn.itcast.action;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.utils.BaseAction;
import cn.itcast.service.AreaService;
import cn.itcast.utils.PinYin4jUtils;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 区域设置的Action
 *
 * @author 刘相磊
 * @version 1.0, 2017-10-27 20:03
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class AreaAction extends BaseAction<Area> {

    /** 通过类型自动注入 */
    @Autowired
    private AreaService areaService;

    /** 上传的文件 */
    private File file;

    /** 上传的文件的名称 */
    private String fileFileName;

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public void setFile(File file) {
        this.file = file;
    }

    /**
     * 导入省市区Execl表格
     *
     * @return
     * @throws IOException
     */
    @Action(value = "AreaExeclImport")
    public String batchImport() throws IOException {
        // 加载Execl对象
        Workbook workbook = null;
        if(fileFileName.endsWith(".xsl")){
            workbook = new HSSFWorkbook(new FileInputStream(file));
        }else {
            workbook = new XSSFWorkbook(new FileInputStream(file));
        }

        // 读取一个Sheet
        Sheet sheetAt = workbook.getSheetAt(0);
        // 读取Sheet中的每一行
        List<Area> areas = new ArrayList<>();
        for (Row row : sheetAt) {
            if (row.getRowNum() == 0) {
                continue;
            }
            if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                continue;
            }
            Area area = new Area();
            // 封装区域ID
            area.setId(row.getCell(0).getStringCellValue());
            // 封装省
            area.setProvince(row.getCell(1).getStringCellValue());
            // 封装城市
            area.setCity(row.getCell(2).getStringCellValue());
            // 封装区域
            area.setDistrict(row.getCell(3).getStringCellValue());
            // 封装邮编
            area.setPostcode(row.getCell(4).getStringCellValue());
            // 每封装一条数据则装入集合中
            areas.add(area);

            // 基于pinyin4j生成城市编码,简码
            // 省
            String province = area.getProvince().substring(0,area.getProvince().length() -1);
            // 城市
            String city = area.getCity().substring(0,area.getCity().length() -1);
            // 区域
            String district = area.getDistrict().substring(0,area.getDistrict().length() -1);
            // 简码
            String[] headByString = PinYin4jUtils.getHeadByString(province + city + district);
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : headByString) {
                stringBuilder.append(s);
            }
            area.setShortcode(stringBuilder.toString());

            // 城市编码
            area.setCitycode(PinYin4jUtils.hanziToPinyin(city,""));

        }
        // 调用业务层保存数据
        areaService.saveBatch(areas);
        return NONE;
    }


    /**
     * 导出Execl表格
     *
     */
    @Action(value = "AreaExport")
    public void areaExport(){
        // 查询所有的区域
        List<Area> areas = areaService.findAll();
        // 创建Execl并写入
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建一个工作簿
        HSSFSheet sheet = workbook.createSheet("区域数据");
        // 遍历集合,创建每一行
        // 创建表头
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("区域编号");
        row.createCell(1).setCellValue("省份");
        row.createCell(2).setCellValue("城市");
        row.createCell(3).setCellValue("区域");
        row.createCell(4).setCellValue("邮编");
        for (int i = 0; i < areas.size(); i++) {
            Area area = areas.get(i);
            HSSFRow sheetRow = sheet.createRow(i+1);
            sheetRow.createCell(0).setCellValue(area.getId());
            sheetRow.createCell(1).setCellValue(area.getProvince());
            sheetRow.createCell(2).setCellValue(area.getCity());
            sheetRow.createCell(3).setCellValue(area.getDistrict());
            sheetRow.createCell(4).setCellValue(area.getPostcode());
        }
        // 提供下载操作
        String fileName = "City.xls";
        // 获取文件的mime类型
        String mimeType = ServletActionContext.getServletContext().getMimeType(fileName);
        // 设置两个头
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType(mimeType);

        response.setHeader("content-disposition", "attachment;filename="+fileName);
        try {
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 复杂条件分区查询
     *
     * @return
     */
    @Action(value = "FindAll",results = {@Result(name = "success",type = "json")})
    public String areaFindAll(){
        Pageable pageable = new PageRequest(page-1,rows);
        final List<Predicate> list = new ArrayList<>();
        Specification<Area> specification = new Specification<Area>() {
            @Override
            public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(StringUtils.isNotBlank(model.getProvince())){
                    Predicate p1 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
                    list.add(p1);
                }
                if(StringUtils.isNotBlank(model.getCity())){
                    Predicate p2 = cb.like(root.get("city").as(String.class), model.getCity());
                    list.add(p2);
                }
                if(StringUtils.isNotBlank(model.getDistrict())){
                    Predicate p3 = cb.like(root.get("district").as(String.class), model.getDistrict());
                    list.add(p3);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        Page<Area> pageDate = areaService.findPageDate(specification, pageable);
        pushPageDataToValueStack(pageDate);
        return SUCCESS;
    }

    /**
     * 查询全部信息
     * @return
     */
    @Action(value = "SubAreaFindByArea",results = {@Result(name = "success",type = "json")})
    public String SubAreaFindByArea (){
        List<Area> areas = areaService.findAll();
        ActionContext.getContext().getValueStack().push(areas);
        return SUCCESS;

    }

    /**
     * 查询省
     * @return
     */
    @Action(value = "FindProvince",results = {@Result(name = "success",type = "json")})
    public String findProvince(){
        List<String> list = areaService.findProvince();
        List<Area> areas = new ArrayList<Area>();
        for (String province : list) {
            Area area = new Area();
            area.setProvince(province);
            areas.add(area);
        }
        ActionContext.getContext().getValueStack().push(areas);
        return SUCCESS;
    }

    /**
     * 查询市
     *
     * @return
     */
    @Action(value = "FindCity",results = {@Result(name = "success",type = "json")})
    public String findCity() throws UnsupportedEncodingException {
        String province = ServletActionContext.getRequest().getParameter("province");
        province = new String(province.getBytes("ISO-8859-1"),"UTF-8");
        List<String> list = areaService.findCity(province);
        List<Area> areas = new ArrayList<>();
        for (String city : list) {
            Area area = new Area();
            area.setCity(city);
            areas.add(area);
        }
        ActionContext.getContext().getValueStack().push(areas);
        return SUCCESS;
    }

    /**
     * 根据省市,查询区县
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    @Action(value = "loadDistrict",results = {@Result(name = "success",type = "json")})
    public String loadDistrict() throws UnsupportedEncodingException {
        String province = model.getProvince();
        province = new String(province.getBytes("ISO-8859-1"),"UTF-8");

        String city = model.getCity();
        city = new String(city.getBytes("ISO-8859-1"),"UTF-8");
        List<String> list = areaService.findDistrict(province,city);
        List<Area> areas = new ArrayList<>();
        for (String district : list) {
            Area area = new Area();
            area.setDistrict(district);
            areas.add(area);
        }
        ActionContext.getContext().getValueStack().push(areas);
        return SUCCESS;
    }

    @Action(value = "findArea",results = {@Result(name = "success",type = "json")})
    public String findArea(){

        Area area = areaService.findArea(model);
        ActionContext.getContext().getValueStack().push(area);
        return SUCCESS;
    }

}

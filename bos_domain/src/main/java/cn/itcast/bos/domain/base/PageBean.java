package cn.itcast.bos.domain.base;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;

/**
 * 自定义分页数据对象
 *
 * @author 刘相磊
 * @version 1.0, 2017-11-07 14:50
 */
@XmlRootElement(name = "pageBean")
@XmlSeeAlso({Promotion.class})
public class PageBean<T> {

    /** 总记录数 */
    private long totalCount;

    /** 当前页数据 */
    private List<T> pageDate;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getPageDate() {
        return pageDate;
    }

    public void setPageDate(List<T> pageDate) {
        this.pageDate = pageDate;
    }
}

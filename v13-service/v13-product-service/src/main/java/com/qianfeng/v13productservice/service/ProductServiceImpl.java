package com.qianfeng.v13productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qianfeng.v13.api.IProductService;
import com.qianfeng.v13.common.base.BaseServiceImpl;
import com.qianfeng.v13.common.base.IBaseDao;
import com.qianfeng.v13.entity.TProduct;
import com.qianfeng.v13.entity.TProductDesc;
import com.qianfeng.v13.mapper.TProductDescMapper;
import com.qianfeng.v13.mapper.TProductMapper;
import com.qianfeng.v13.pojo.TProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author huangguizhao
 */
@Service
public class ProductServiceImpl extends BaseServiceImpl<TProduct> implements IProductService{

    @Autowired
    private TProductMapper productMapper;

    @Autowired
    private TProductDescMapper productDescMapper;

    @Override
    public IBaseDao<TProduct> getBaseDao() {
        return productMapper;
    }

    @Override
    public PageInfo<TProduct> page(Integer pageIndex, Integer pageSize) {
        //1.设置分页参数
        PageHelper.startPage(pageIndex,pageSize);
        //2.获取数据
        List<TProduct> list = list();
        //3.构建一个分页对象
        PageInfo<TProduct> pageInfo = new PageInfo<TProduct>(list,2);
        return pageInfo;
    }

    @Override
    @Transactional
    public Long save(TProductVO vo) {
        //1.保存商品的基本信息
        TProduct product = vo.getProduct();
        product.setFlag(true);
        //主键回填
        int count = productMapper.insert(product);
        //2.保存商品的描述信息
        String productDesc = vo.getProductDesc();
        TProductDesc desc = new TProductDesc();
        desc.setProductDesc(productDesc);
        desc.setProductId(product.getId());
        productDescMapper.insert(desc);
        //3.返回新增商品的主键
        return product.getId();
    }

    @Override
    public Long batchDel(List<Long> ids) {
        //update t_product set flag=0 where id in(1,2,3)
        return productMapper.batchUpdateFlag(ids);
    }

    /**
     * 由于我们现在是逻辑删除，所以需要重写
     * @param id
     * @return
     */
    @Override
    public int deleteByPrimaryKey(Long id) {
        // update t_product set flag=0 where id=#{id}
        TProduct product = new TProduct();
        product.setId(id);
        product.setFlag(false);
        return productMapper.updateByPrimaryKeySelective(product);
    }
}

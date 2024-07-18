package com.example.service;

import com.example.entity.Category;

import com.example.entity.Course;
import com.example.entity.Impact_Area;
import com.example.entity.Institution;
import com.example.mapper.ListMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ListService {
    @Resource
    private ListMapper listMapper;

    //total是查询的总数,list是数据列表
    //pageNum是当前页码,pageSize是每页个数
    public PageInfo<Institution> selectPage(int pageNum, int pageSize, Institution institution){
        PageHelper.startPage(pageNum,pageSize);
        List<Institution> institutionList = listMapper.selectAll(institution);
        return PageInfo.of(institutionList);
    }

    //新增数据
    public void add(Institution institution) {
        listMapper.insert(institution);
    }

    public void updateById(Institution institution) {
        listMapper.updateById(institution);
    }

    @Transactional
    public void updatePoint(Institution institution) {
        for (Category category : institution.getCategories()) {
            for (Impact_Area impact_area : category.getImpact_areas()) {
                listMapper.updateImpactAreaPoint(impact_area.getId(), impact_area.getPoint());
            }
        }
    }

    public void deleteById(Integer id) {
        for (Category category: findCategoriesByInstitutionId(id)){
            listMapper.deleteImpactArea(category.getId());
        }
        listMapper.deleteCategory(id);
        listMapper.deleteById(id);
    }

    public List<Institution> searchInstitutions(String query) {
        return listMapper.findByNameContainingIgnoreCase(query);
    }

    public Institution findById(Integer id) {
        return listMapper.selectById(id);
    }

    public List<Category> findCategoriesByInstitutionId(Integer institutionId) {
        return listMapper.selectCategoriesByInstitutionId(institutionId);
    }

    public List<Impact_Area> findImpactAreasByInstitutionId(Integer categoryId) {
        return listMapper.selectImpactAreasByCategoryId(categoryId);
    }


}

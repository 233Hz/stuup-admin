package com.poho.stuup.model.vo;

import com.poho.stuup.model.Menu;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuTree extends Menu {

    private List<MenuTree> children;

}
package com.rui.pro1.modules.sys.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rui.pro1.common.annotatiions.MenuAnnot;
import com.rui.pro1.common.annotatiions.PermissionAnnot;
import com.rui.pro1.common.bean.ResultBean;
import com.rui.pro1.common.bean.page.QueryResult;
import com.rui.pro1.common.constants.Modules;
import com.rui.pro1.common.constants.uri.SysUri;
import com.rui.pro1.common.exception.MessageCode;
import com.rui.pro1.modules.sys.bean.RoleBean;
import com.rui.pro1.modules.sys.bean.UserBean;
import com.rui.pro1.modules.sys.entity.Role;
import com.rui.pro1.modules.sys.exception.ObjectExistException;
import com.rui.pro1.modules.sys.service.IRoleService;
import com.rui.pro1.modules.sys.vo.RoleVo;

/**
 * 用户管理
 * 
 * @author ruiliang
 * @date 2016/04/05
 *
 */
@Controller
@RequestMapping(SysUri.SYS_ROLE)
@MenuAnnot(id = "sys:role", name = "角色管理", parentId = Modules.SYS, href = "/views/modules/sys/role/rolelist", sortNo = 2)
public class RoleController extends SysBaseController {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IRoleService roleService;

	@PermissionAnnot(id = "sys:role:list", name = "查看列表")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean getList(
			@RequestParam(value = "pageIndex", defaultValue = "1") Integer page,
			@RequestParam(value = "pagesize", defaultValue = "15") Integer pagesize,
			RoleVo roleVo) {
		ResultBean rb = new ResultBean();

		QueryResult<Role> result = roleService.getRoleList(page, pagesize,
				roleVo);
		rb.setData(result);

		return rb;

	}

	// @PermissionAnnot(id = MenuSys.SYS_ROLE + ":list", name = "查看列表")
	@RequestMapping(value = "listAll", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean getListAll() {
		ResultBean rb = new ResultBean();

		List<Role> result = roleService.getRoleListAll();
		rb.setData(result);

		return rb;

	}

	@RequestMapping(value = "get", method = RequestMethod.GET)
	@PermissionAnnot(id = "sys:role:get", name = "查看")
	@ResponseBody
	public ResultBean get(HttpServletRequest request,
			HttpServletResponse response, RoleVo roleVo) {
		ResultBean rb = new ResultBean();

		RoleBean role = roleService.get(roleVo.getId());
		rb.setData(role);

		return rb;
	}

	@PermissionAnnot(id = "sys:role:del", name = "删除")
	@RequestMapping(value = "del", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean del(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "id") Integer id) {
		ResultBean rb = new ResultBean();

		int count = roleService.del(id);
		if (count <= 0) {
			rb = new ResultBean(false, MessageCode.SYS_FAILURE, "操作失败");
		}

		return rb;
	}

	@PermissionAnnot(id = "sys:role:add", name = "添加")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean add(HttpServletRequest request,
			HttpServletResponse response, Role role) {
		ResultBean rb = new ResultBean();

		try {

			UserBean user = userUtils.getUserBean();
			role.setUpdateById(user.getId());
			roleService.add(role);
		} catch (ObjectExistException e) {
			rb = new ResultBean(false, MessageCode.ROLE_EXISTS, "角色已存在");
		} catch (Exception e) {
			e.printStackTrace();
			rb = new ResultBean(false, MessageCode.SYS_ERROR, "异统异常");
		}

		return rb;
	}

	@PermissionAnnot(id = "sys:role:update", name = "修改")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean update(HttpServletRequest request,
			HttpServletResponse response, Role role) {
		ResultBean rb = new ResultBean();
		roleService.update(role);
		return rb;
	}

}

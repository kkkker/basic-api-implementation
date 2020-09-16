package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = initRsList();

  private List<RsEvent> initRsList() {
    List<RsEvent> tempRsList = new ArrayList<>();
    tempRsList.add(new RsEvent("第一条事件", "无分类"));
    tempRsList.add(new RsEvent("第二条事件", "无分类"));
    tempRsList.add(new RsEvent("第三条事件", "无分类"));
    return tempRsList;
  }

  @GetMapping("/rs/event/{index}")
  public RsEvent getOneRsEvent(@PathVariable int index) {
    return rsList.get(index - 1);
  }

  @GetMapping("/rs/event")
  public List<RsEvent> getRsEventByRange(@RequestParam(required = false) Integer start,
                                         @RequestParam(required = false) Integer end) {
    if (start == null || end == null) {
      return rsList;
    }
    return rsList.subList(start - 1, end);
  }

  @PostMapping("/rs/add/event")
  public void addOneRsEvent(@Valid @RequestBody RsEvent rsEvent) {
    if (!UserController.userList.contains(rsEvent.getUser())) {
      UserController.userList.add(rsEvent.getUser());
    }
    rsList.add(rsEvent);
  }

  @PutMapping("/rs/update/event/{index}")
  public String updateRsEventByIndex(@PathVariable Integer index, @RequestBody RsEvent rsEvent) {
    if (index == null || rsList.size() < index) {
      return "更新失败";
    }
    RsEvent thisRsEvent = rsList.get(index - 1);
    if (rsEvent.getEventName() != null) {
      thisRsEvent.setEventName(rsEvent.getEventName());
    }
    if (rsEvent.getKeyword() != null) {
      thisRsEvent.setKeyword(rsEvent.getKeyword());
    }
    return "更新成功";
  }

  @DeleteMapping("/rs/delete/event/{index}")
  public String deleteRsEventByIndex(@PathVariable Integer index) {
    if (index == null || rsList.size() < index) {
      return "删除失败";
    }
    rsList.remove(index - 1);
    return "删除成功";
  }
}

package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.exception.EventIndexException;
import com.thoughtworks.rslist.exception.EventRangeException;
import com.thoughtworks.rslist.exception.ExceptionMessage;
import com.thoughtworks.rslist.exception.HandleException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

  @Autowired
  RsEventRepository rsEventRepository;

  @Autowired
  UserRepository userRepository;

  @GetMapping("/rs/event/{index}")
  public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int index) throws EventIndexException {
    if (index > rsList.size()) {
      throw new EventIndexException();
    }
    return ResponseEntity.ok().body(rsList.get(index - 1));
  }

  @GetMapping("/rs/event")
  public ResponseEntity<List<RsEvent>> getRsEventByRange(@RequestParam(required = false) Integer start,
                                         @RequestParam(required = false) Integer end) throws EventRangeException {
    if (start == null || end == null) {
      return ResponseEntity.ok().body(rsList);
    }
    if (start < 1 || start > rsList.size() || end < start || end > rsList.size()) {
      throw new EventRangeException();
    }
    return ResponseEntity.ok().body(rsList.subList(start - 1, end));
  }

  @PostMapping("/rs/add/event")
  public ResponseEntity<Object> addOneRsEvent(@Valid @RequestBody RsEvent rsEvent) {

    if (!userRepository.existsById(rsEvent.getUserId())) {
      return ResponseEntity.status(400)
              .build();
    }
    RsEventEntity rsEventEntity = RsEventEntity.builder()
            .eventName(rsEvent.getEventName())
            .userId(rsEvent.getUserId())
            .keyword(rsEvent.getKeyword())
            .build();
    rsEventRepository.save(rsEventEntity);
    return ResponseEntity.status(201)
            .header("index", String.valueOf(rsEventRepository.findAll().indexOf(rsEventEntity) + 1))
            .build();
  }

  @PutMapping("/rs/update/event/{index}")
  public ResponseEntity<String> updateRsEventByIndex(@PathVariable Integer index, @RequestBody RsEvent rsEvent) {
    if (index == null || rsList.size() < index) {
      return ResponseEntity.ok().body("更新失败");
    }
    RsEvent thisRsEvent = rsList.get(index - 1);
    if (rsEvent.getEventName() != null) {
      thisRsEvent.setEventName(rsEvent.getEventName());
    }
    if (rsEvent.getKeyword() != null) {
      thisRsEvent.setKeyword(rsEvent.getKeyword());
    }
    return ResponseEntity.ok().body("更新成功");
  }

  @DeleteMapping("/rs/delete/event/{index}")
  public ResponseEntity<String> deleteRsEventByIndex(@PathVariable Integer index) {
    if (index == null || rsList.size() < index) {
      return ResponseEntity.ok().body("删除失败");
    }
    rsList.remove(index - 1);
    return ResponseEntity.ok().body("删除成功");
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<ExceptionMessage> handleException(Exception ex) {
    ExceptionMessage exceptionMessage = new ExceptionMessage();
    Logger logger = LoggerFactory.getLogger(RsController.class);
    if (ex instanceof MethodArgumentNotValidException) {
      exceptionMessage.setError("invalid param");
      logger.error(exceptionMessage.getError());
      return ResponseEntity.status(400).body(exceptionMessage);
    }
    logger.error(exceptionMessage.getError());
    return ResponseEntity.status(400).body(null);
  }
}

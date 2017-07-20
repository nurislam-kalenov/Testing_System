package services;

import dao.UserTestDao;
import dao.exception.DaoException;
import dao.manager.DaoFactory;
import entity.UserTest;

import java.util.Date;

/**
 * Created by User on 19.07.2017.
 */
public class UserTestService {

    public UserTest createTest(UserTest userTest) {
        try (DaoFactory daoFactory = new DaoFactory()) {
            try {
                userTest.setEndDate(new Date());
                UserTestDao userTestDao = (UserTestDao) daoFactory.getDao(daoFactory.typeDao().getUserTestDao());
                daoFactory.startTransaction();
                userTestDao.insert(userTest);
                daoFactory.commitTransaction();
            } catch (DaoException e) {
                daoFactory.rollbackTransaction();
                e.printStackTrace();
            }
        }
        return userTest;
    }


    public UserTest findUserTestById(int id) {
        UserTest userTest = null;
        try (DaoFactory daoFactory = new DaoFactory()) {
            try {
                UserTestDao userTestDao = (UserTestDao) daoFactory.getDao(daoFactory.typeDao().getUserTestDao());
                userTest = userTestDao.findById(id);
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
        return userTest;
    }
}
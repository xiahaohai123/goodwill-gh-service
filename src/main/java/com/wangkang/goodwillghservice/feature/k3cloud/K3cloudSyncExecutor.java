package com.wangkang.goodwillghservice.feature.k3cloud;

import com.wangkang.goodwillghservice.core.exception.BusinessException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class K3cloudSyncExecutor {

    private static final Log log = LogFactory.getLog(K3cloudSyncExecutor.class);
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Scheduler：只尝试一次
     */
    public <T> Optional<T> tryExecuteOnce(Callable<T> task) {
        if (!lock.tryLock()) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(task.call());
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Controller：有限次数 + 等待
     */
    public <T> Optional<T> executeWithRetry(
            Callable<T> task,
            int maxRetries,
            long waitMillis
    ) throws InterruptedException {

        for (int i = 1; i <= maxRetries; i++) {
            if (lock.tryLock(waitMillis, TimeUnit.MILLISECONDS)) {
                try {
                    return Optional.ofNullable(task.call());
                } catch (Exception e) {
                    throw new BusinessException(e);
                } finally {
                    lock.unlock();
                }
            }
            log.warn("Failed to acquire sync lock, retry " + i + "/" + maxRetries);
        }
        return Optional.empty();
    }
}
